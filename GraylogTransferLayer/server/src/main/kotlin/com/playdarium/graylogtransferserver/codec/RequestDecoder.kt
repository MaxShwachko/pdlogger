package com.playdarium.graylogtransferserver.codec

import com.playdarium.byteformatter.ByteBuffer
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.RequestReaderManager
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

class RequestDecoder(private val requestReaderManager: RequestReaderManager) {

    fun decode(remoteHost: String, buffer: ByteBuffer): Result {
        val header = buffer.readByte()
        log.info { "header: $header" }
        if (!requestReaderManager.checkRequestHeader(header)) {
            return Result(header, null)
        }

        val request = requestReaderManager.readRequest(remoteHost, header, buffer)
        return Result(header, request)
    }

    data class Result(val header: Byte, val request: Request?)
}