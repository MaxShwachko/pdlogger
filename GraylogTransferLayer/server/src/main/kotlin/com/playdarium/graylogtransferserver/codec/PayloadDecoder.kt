package com.playdarium.graylogtransferserver.codec

import com.playdarium.byteformatter.ByteBuffer
import com.playdarium.byteformatter.ByteReader
import io.vertx.core.buffer.Buffer
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

class PayloadDecoder {

    fun decode(buffer: Buffer): ByteBuffer? {
        if (buffer.length() < 3) {
            return null
        }

        val bytes = buffer.bytes

        val reader = ByteReader.create(bytes)
        try {
            val length = reader.readShort()
            val payload = reader.readBytes(length.toInt())
            return ByteBuffer.create(payload)
        } catch (e: Exception) {
            log.error("Error occurred, bytes: ${bytes.size}", e) //ctx.close();
        }

        return null
    }
}