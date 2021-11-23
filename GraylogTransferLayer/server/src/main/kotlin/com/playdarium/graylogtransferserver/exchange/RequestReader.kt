package com.playdarium.graylogtransferserver.exchange

import com.playdarium.byteformatter.ByteReader

interface RequestReader {
    fun readRequest(remoteHost: String, reader: ByteReader): Request
}