package org.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketAddress
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val logger: Logger = LoggerFactory.getLogger("Main")
val totalSessions = AtomicInteger(0)
val activeSessions = AtomicInteger(0)
val ipSessions = mutableMapOf<SocketAddress, Int>()
val threadList = mutableListOf<Thread>()

const val PORT = 9090
fun main() {
    logger.info("Starting echo server on port $PORT")
    runEchoServer()
    threadList.map { it.join(10000) }
    logger.info("Echo server stopped")
}

fun runEchoServer() {
    ServerSocket().use { serverSocket ->
        serverSocket.bind(InetSocketAddress("0.0.0.0", PORT))
        val clientLock = ReentrantLock()
        while (true) {
            logger.info("Waiting for clients to connect...")
            val clientSocket = serverSocket.accept()
            threadList.addLast(Thread.ofPlatform().start {
                val count = totalSessions.incrementAndGet() // This is wrong! Read-modify-write is not atomic
                handleClient(clientSocket, count, clientLock)
            })
        }
    }
}

fun handleClient(clientSocket: Socket, clientCount: Int, lock: ReentrantLock) {
    logger.info("Client connected: ${clientSocket.inetAddress.hostAddress}")
    clientSocket.use {
        it.getInputStream().bufferedReader().use { reader ->
            it.getOutputStream().bufferedWriter().use { writer ->
                lock.withLock {
                    if (ipSessions[it.remoteSocketAddress] == null)
                        ipSessions[it.remoteSocketAddress] = 1
                    else
                        ipSessions[it.remoteSocketAddress] = ipSessions[it.remoteSocketAddress]!! + 1
                }
                activeSessions.set(activeSessions.incrementAndGet())
                logger.info("Handling client #$clientCount")
                writer.writeLine("Hello, client #$clientCount! Please type something and press Enter:")
                var line = reader.readLine()
                while (line.lowercase() != ":quit") {
                    if (line.lowercase() == ":counters") {
                        lock.withLock {
                            writer.writeLine("You are client #$clientCount")
                            writer.writeLine("Total client connections: ${totalSessions.get()}")
                            writer.writeLine("Currently active connections: ${activeSessions.get()}")
                            val userSessions = ipSessions[it.remoteSocketAddress]!!
                            writer.writeLine("You visited us $userSessions " +
                                                 "${if(userSessions == 1) "time" else "times"} before")
                        }
                    }
                    else
                        writer.writeLine("You wrote: \"$line\"")
                    line = reader.readLine()
                }
                writer.writeLine("Bye!\n")
                activeSessions.set(activeSessions.decrementAndGet())
            }
        }
    }
}

private fun BufferedWriter.writeLine(line: String) {
    write(line)
    newLine()
    flush()
}