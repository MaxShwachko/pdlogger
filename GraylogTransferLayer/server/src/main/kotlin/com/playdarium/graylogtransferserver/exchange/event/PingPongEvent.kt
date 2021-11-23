package com.playdarium.graylogtransferserver.exchange.event

import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.Event
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.response.PingPongResp


class PingPongEvent : Event {
    override fun handle(encoder: ResponseEncoder, request: Request) {
        encoder.write(PingPongResp())
    }
}
