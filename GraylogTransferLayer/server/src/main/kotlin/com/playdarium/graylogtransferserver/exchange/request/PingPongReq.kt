package com.playdarium.graylogtransferserver.exchange.request

import com.playdarium.byteformatter.ByteReader
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.RequestReader

class PingPongReq : Request {
    override val header: Byte = Header.PingPong.value

    class Reader : RequestReader {
        override fun readRequest(remoteHost: String, reader: ByteReader): Request {
            return PingPongReq()
        }
    }
}