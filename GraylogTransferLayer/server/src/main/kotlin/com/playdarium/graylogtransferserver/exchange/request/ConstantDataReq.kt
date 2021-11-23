package com.playdarium.graylogtransferserver.exchange.request

import com.playdarium.byteformatter.ByteReader
import com.playdarium.graylogtransferserver.exchange.Header
import com.playdarium.graylogtransferserver.exchange.Request
import com.playdarium.graylogtransferserver.exchange.RequestReader
import com.playdarium.graylogtransferserver.model.ConstantData

class ConstantDataReq(val constantData: ConstantData) : Request {

    override val header: Byte = Header.ConstantData.value

    class Reader : RequestReader {
        override fun readRequest(remoteHost: String, reader: ByteReader): Request {
            val constantData = ConstantData()

            constantData.source = reader.readString()
            constantData.os = reader.readString()
            constantData.osVersion = reader.readString()
            constantData.device = reader.readString()
            constantData.deviceUid = reader.readString()
            constantData.resolution = reader.readString()
            constantData.appVersion = reader.readString()
            constantData.appId = reader.readString()
            constantData.appName = reader.readString()
            constantData.cpu = reader.readString()
            constantData.opengl = reader.readString()
            constantData.memorySize = reader.readShort()
            constantData.gpuMemorySize = reader.readShort()
            constantData.orientation = reader.readByte()

            return ConstantDataReq(constantData)
        }
    }
}