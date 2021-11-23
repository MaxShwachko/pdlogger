package com.playdarium.graylogtransferserver.model

import sun.jvmstat.monitor.remote.RemoteHost
import java.util.HashMap

class Log(val remoteHost: String) {
    var shortMessage: String = ""
    var timestamp: Long = 0
    var logLevel: Byte = 0
    var battery: Float = 0f
    var online: Byte = 0

    val additionalFields: MutableMap<String, String> = HashMap()
}
