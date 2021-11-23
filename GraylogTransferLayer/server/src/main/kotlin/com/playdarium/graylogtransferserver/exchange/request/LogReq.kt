package com.playdarium.graylogtransferserver.exchange.request

import com.playdarium.byteformatter.ByteReader
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.RequestReader
import com.playdarium.graylogtransferserver.model.Log

class LogReq(val log: Log) : Request {

    override val header: Byte = Header.Log.value

    class Reader : RequestReader {

        override fun readRequest(remoteHost: String, reader: ByteReader): Request {
            val log = Log(remoteHost)
            log.shortMessage =  reader.readString()
            log.timestamp = reader.readLong()
            log.logLevel = reader.readByte()

            val additionalFieldsCount = reader.readByte()
            for (i in 0 until additionalFieldsCount) {
                val key = reader.readString()
                val value = reader.readString()
                log.additionalFields[key] = value
            }
            return LogReq(log)
        }
    }
}