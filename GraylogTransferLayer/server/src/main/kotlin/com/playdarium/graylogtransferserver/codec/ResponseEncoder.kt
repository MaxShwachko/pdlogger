package com.playdarium.graylogtransferserver.codec

import com.playdarium.byteformatter.ByteBuffer
import com.playdarium.byteformatter.ByteWriter
import com.playdarium.graylogtransferserver.exchange.Response
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class ResponseEncoder(private val socket: NetSocket) {
    var counter = 0;

    fun write(response: Response) {
        val bytes = getResponseBytes(response);
        val payload = Buffer.buffer(bytes)
        socket.write(payload)
        counter++;
        log.info { "payload length: ${payload.length()}, counter: $counter" }
    }

    fun close() {
        socket.close()
    }

    private fun getResponseBytes(response: Response): ByteArray {
        val buffer = ByteBuffer.createDefault()
        val writer = ByteWriter.create(buffer)
        val lengthBytes = 2
        writer.skip(lengthBytes) //first 2 bytes for length (short)
        writer.write(response.header)

        response.compose(writer)

        val actualLength = writer.position
        val payloadLength = actualLength - lengthBytes
        writer.seekZero()
        writer.write(payloadLength.toShort())
        writer.skip(payloadLength.toInt())

        return writer.array()
    }

    val remoteHost: String
        get() = this.socket.remoteAddress().host()
}