package com.playdarium.graylogtransferserver.exchange.request

import com.playdarium.byteformatter.ByteReader
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.RequestReader
import com.playdarium.graylogtransferserver.util.Version
import com.playdarium.graylogtransferserver.util.Version.Companion.fromString

class HandshakeRequest(val authToken: String, val apiVersion: Version, val sessionToken: String?) : Request {

    override val header: Byte = Header.Handshake.value

    class Reader : RequestReader {
        override fun readRequest(remoteHost: String, reader: ByteReader): Request {
            val authToken = reader.readString()
            val apiVersionStr = reader.readString()
            val sessionToken = if (reader.readableBytes() > 0) reader.readString() else null
            val apiVersion = fromString(apiVersionStr)
            return HandshakeRequest(authToken, apiVersion, sessionToken)
        }
    }
}