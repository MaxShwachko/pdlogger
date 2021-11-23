package com.playdarium.graylogtransferserver.exchange.event

import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.Event
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.request.ConstantDataReq
import com.playdarium.graylogtransferserver.model.User
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

class ConstantDataEvent(private val users: ConcurrentHashMap<String, User>) : Event {
    override fun handle(encoder: ResponseEncoder, request: Request) {
        val req = request as ConstantDataReq


        users[encoder.remoteHost]?.constantData =  req.constantData

        log.info { "Set constant data for ${encoder.remoteHost}, data: " + req.constantData.source }
    }
}