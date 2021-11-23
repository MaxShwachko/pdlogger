package com.playdarium.graylogtransferserver.util

fun ByteArray.asString(): String {
    val sb = StringBuilder().append("new byte[] { ")
    for (b in this) {
        sb.append(b).append(", ")
    }
    sb.append("}")
    return sb.toString()
}