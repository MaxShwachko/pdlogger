package com.playdarium.graylogtransferserver.exchange.response

import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Response
import com.playdarium.byteformatter.ByteWriter

class PingPongResp : Response {
    override val header: Byte = Header.PingPong.value

    override fun compose(writer: ByteWriter) {}
}