package com.playdarium.graylogtransferserver.exchange

import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.Request
import sun.jvmstat.monitor.remote.RemoteHost

interface Event {
    fun handle(encoder: ResponseEncoder, request: Request)
}