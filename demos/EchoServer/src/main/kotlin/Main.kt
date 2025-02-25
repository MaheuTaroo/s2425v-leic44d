package org.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val logger: Logger = LoggerFactory.getLogger("Main")

const val PORT = 9090
fun main() {
    logger.info("Starting echo server on port $PORT")
    runEchoServer()
    logger.info("Echo server stopped")
}

fun runEchoServer() {
    ServerSocket().use { serverSocket ->
        serverSocket.bind(InetSocketAddress("0.0.0.0", PORT))
        var clientCount = 0
        val lock = ReentrantLock()
        while (true) {
            logger.info("Waiting for clients to connect...")
            val clientSocket = serverSocket.accept()
            Thread.ofPlatform().start {
                clientCount += 1 // This is wrong! Read-modify-write is not atomic
                handleClient(clientSocket, clientCount)
            }
        }
    }
}

fun handleClient(clientSocket: Socket, clientCount: Int) {
    logger.info("Client connected: ${clientSocket.inetAddress.hostAddress}")
    clientSocket.use {
        it.getInputStream().bufferedReader().use { reader ->
            it.getOutputStream().bufferedWriter().use { writer ->
                logger.info("Handling client #$clientCount")
                writer.writeLine("Hello, client #$clientCount! Please type something and press Enter:")
                val line = reader.readLine()
                writer.writeLine("You wrote: $line")
                writer.writeLine("Bye!\n")
            }
        }
    }
}

private fun BufferedWriter.writeLine(line: String) {
    write(line)
    newLine()
    flush()
}