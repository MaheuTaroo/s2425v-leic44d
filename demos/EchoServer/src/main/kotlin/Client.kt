package org.example

import java.net.Socket

fun main() {
    val list = mutableListOf<Thread>()
    repeat(10) {
        list.addLast(Thread.ofPlatform().start {
            Socket("127.0.0.1", 9090).use { sock ->
                sock.getInputStream().bufferedReader().use { reader ->
                    sock.getOutputStream().bufferedWriter().use { writer ->
                        println("Thread ${it + 1} got ${reader.readLine()}")
                        writer.write(":counters")
                        writer.newLine()
                        writer.flush()
                        println("Thread ${it + 1} got the following counters:")
                        repeat(3) { _ -> println("\tThread ${it + 1}: \"${reader.readLine()}\"")}
                        writer.write("abc")
                        writer.newLine()
                        writer.flush()
                        println("Thread ${it + 1} got: ${reader.readLine()}")
                        writer.write(":quit")
                        writer.newLine()
                        writer.flush()
                        println("Thread ${it + 1} left")
                    }
                }
            }

        })
    }
    list.map(Thread::join)
}