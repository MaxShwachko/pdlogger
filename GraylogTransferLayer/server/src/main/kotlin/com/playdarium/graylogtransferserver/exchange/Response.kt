package com.playdarium.graylogtransferserver.exchange

import com.playdarium.byteformatter.ByteWriter


interface Response {
    val header: Byte
    fun compose(writer: ByteWriter)
}