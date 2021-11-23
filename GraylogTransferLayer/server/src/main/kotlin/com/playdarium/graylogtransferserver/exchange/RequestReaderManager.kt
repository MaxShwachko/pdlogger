package com.playdarium.graylogtransferserver.exchange

import com.playdarium.byteformatter.ByteBuffer
import com.playdarium.byteformatter.ByteReader
import com.playdarium.graylogtransferserver.exchange.request.ConstantDataReq
import com.playdarium.graylogtransferserver.exchange.request.HandshakeRequest
import com.playdarium.graylogtransferserver.exchange.request.LogReq
import com.playdarium.graylogtransferserver.exchange.request.PingPongReq
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

class RequestReaderManager {

    private val readers = ConcurrentHashMap<Byte, RequestReader>()

    init {
        registerRequestReader(Header.Handshake.value, HandshakeRequest.Reader())
        registerRequestReader(Header.PingPong.value, PingPongReq.Reader())
        registerRequestReader(Header.ConstantData.value, ConstantDataReq.Reader())
        registerRequestReader(Header.Log.value, LogReq.Reader())
    }

    private fun registerRequestReader(header: Byte, reader: RequestReader) {
        readers[header] = reader
    }

    fun readRequest(remoteHost: String, header: Byte, buffer: ByteBuffer): Request? {
        if (!readers.containsKey(header)) {
            log.error("readRequest -> no reader for header $header")
            return null
        }
        var request: Request? = null
        val reader = readers[header]
        try {
            val byteReader = ByteReader.create(buffer)
            request = reader!!.readRequest(remoteHost, byteReader)
        } catch (e: Exception) {
            log.error("Exchange error", e) //ctx.close();
        }
        return request!!
    }

    fun checkRequestHeader(header: Byte): Boolean {
        for (h in Header.values()) {
            if (h.value == header) return true
        }
        return false
    }
}