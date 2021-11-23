package com.playdarium.graylogtransferserver.model

class User{
    var authToken: String? = null
    var sessionToken: String? = null
    var constantData: ConstantData? = null

    val isLogged: Boolean
        get() = authToken != null && sessionToken != null
}