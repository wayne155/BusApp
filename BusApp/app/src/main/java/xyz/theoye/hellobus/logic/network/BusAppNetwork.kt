package xyz.theoye.hellobus.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import retrofit2.http.Body
import xyz.theoye.hellobus.Settings.verifyCode
import xyz.theoye.hellobus.logic.model.AdminLoginRequest
import xyz.theoye.hellobus.logic.model.AdminLoginResponse
import xyz.theoye.hellobus.logic.model.VerifyCodeRequest
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 程序统一的网络数据源访问入口
 */


object BusAppNetwork {
    private  val LOG_TAG = "BusAppNetwork"
    private val userLoginService = ServiceCreator.codeCreate(UserLoginService::class.java)
    private val searchPlaceService = ServiceCreator.cityCreate(PlaceService::class.java)
    private val adminLoginService = ServiceCreator.create(LoginService::class.java)


    //将函数挂起
    // 请求验证码
    suspend fun requestVerifycode(data: VerifyCodeRequest) = userLoginService.sendCodeRequest(data).await()
    // 请求城市数据接口
    suspend fun searchPlaces(query:String) = searchPlaceService.serachPlaces(query).await()
    suspend fun adminLogin(adminLoginRequest: AdminLoginRequest) = adminLoginService.adminLogin(adminLoginRequest).await()



    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation->
            enqueue( object :Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body!=null) continuation.resume(body)
                    else continuation .resumeWithException(
                        RuntimeException("Response body is null!!!")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation .resumeWithException(t)
                }

            })
        }
    }








}