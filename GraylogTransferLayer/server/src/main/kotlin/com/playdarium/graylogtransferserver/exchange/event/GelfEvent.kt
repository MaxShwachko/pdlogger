package com.playdarium.graylogtransferserver.exchange.event

import com.playdarium.graylogtransferserver.codec.ResponseEncoder
import com.playdarium.graylogtransferserver.exchange.Event
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.request.LogReq
import com.playdarium.graylogtransferserver.model.ScreenOrientation
import com.playdarium.graylogtransferserver.model.User
import org.graylog2.gelfclient.GelfMessage
import org.graylog2.gelfclient.GelfMessageLevel
import org.graylog2.gelfclient.transport.GelfTransport
import java.util.concurrent.ConcurrentHashMap

class GelfEvent(private val gelfTransport: GelfTransport,
                private val userMap: ConcurrentHashMap<String, User>) : Event {

    override fun handle(encoder: ResponseEncoder, request: Request) {
        val logReq = request as LogReq

        val remoteHost = logReq.log.remoteHost
        val constantData = userMap[remoteHost]?.constantData ?: return

        val gelfMessage = GelfMessage(logReq.log.shortMessage, constantData.source)
        gelfMessage.level = GelfMessageLevel.fromNumericLevel(logReq.log.logLevel.toInt())
        gelfMessage.timestamp = logReq.log.timestamp.toDouble()

        gelfMessage.addAdditionalField("os", constantData.os)
        gelfMessage.addAdditionalField("os_version", constantData.osVersion)
        gelfMessage.addAdditionalField("device", constantData.device)
        gelfMessage.addAdditionalField("device_uid", constantData.deviceUid)
        gelfMessage.addAdditionalField("resolution", constantData.resolution)
        gelfMessage.addAdditionalField("app_version", constantData.appVersion)
        gelfMessage.addAdditionalField("app_id", constantData.appId)
        gelfMessage.addAdditionalField("app_name", constantData.appName)
        gelfMessage.addAdditionalField("cpu", constantData.cpu)
        gelfMessage.addAdditionalField("opengl", constantData.opengl)
        gelfMessage.addAdditionalField("memory_size", constantData.memorySize)
        gelfMessage.addAdditionalField("gpu_memory_size", constantData.gpuMemorySize)
        gelfMessage.addAdditionalField("battery", logReq.log.battery)
        gelfMessage.addAdditionalField("orientation", ScreenOrientation.parse(constantData.orientation))
        gelfMessage.addAdditionalField("online", logReq.log.online.compareTo(1))

        for (field in logReq.log.additionalFields) {
            gelfMessage.addAdditionalField(field.key, field.value)
        }

        gelfTransport.trySend(gelfMessage)
    }

}