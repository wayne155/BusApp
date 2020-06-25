package xyz.theoye.hellobus.logic.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import xyz.theoye.hellobus.logic.model.AdminLoginRequest
import xyz.theoye.hellobus.logic.model.AdminLoginResponse
import xyz.theoye.hellobus.logic.model.PlaceResponse
import xyz.theoye.hellobus.logic.model.VerifyCodeRequest

interface LoginService{
    @Headers("Content-Type: application/json")
    @POST("v1/token")
    //TODO() 请求登陆
    fun adminLogin(@Body adminLoginRequest: AdminLoginRequest): Call<AdminLoginResponse>
}