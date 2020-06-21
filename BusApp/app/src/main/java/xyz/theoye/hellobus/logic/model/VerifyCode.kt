package xyz.theoye.hellobus.logic.model

//验证码服务器返回
data class VerifyCodeResponse(val result:String , val desc:String , val msgid:String, val csutid:String )

data class VerifyCodeRequest(val apikey:String , val mobile:String , val content:String ,
                             val svrtype:String="", val exno:String = "",val custid:String ="", val exdata:String ="")

