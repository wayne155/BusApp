package xyz.theoye.hellobus.logic.model

data class AdminLoginRequest(val account:String, val type:Int, val secret:String)

data class AdminLoginResponse(val token:String, val error_code:Int=0, val msg:String="", val request:String="")
