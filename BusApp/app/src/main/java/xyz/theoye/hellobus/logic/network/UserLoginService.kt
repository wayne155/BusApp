package xyz.theoye.hellobus.logic.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import xyz.theoye.hellobus.logic.model.VerifyCodeRequest
import xyz.theoye.hellobus.logic.model.VerifyCodeResponse
import java.net.URLEncoder

interface UserLoginService{

    //发送验证码
    @Headers("Content-Type: application/json")
    @POST("sms/v2/std/single_send")
    fun sendCodeRequest(@Body verifyCodeRequest:VerifyCodeRequest): Call<VerifyCodeResponse>
}

//val verifyCodeRequest =VerifyCodeRequest("9821fcca86fd96b280ea7f87dbba701c", "13305962916",
//    URLEncoder.encode("验证码：789120，打死都不要告诉别人哦！", "GBK"))