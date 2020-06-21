package xyz.theoye.hellobus.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import xyz.theoye.hellobus.Settings.verifyCode
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
    private val userLoginService = ServiceCreator.create(UserLoginService::class.java)

    suspend fun requestVerifycode(data: VerifyCodeRequest) = userLoginService.sendCodeRequest(data).await()

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