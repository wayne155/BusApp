package xyz.theoye.hellobus

import okhttp3.RequestBody
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import xyz.theoye.hellobus.BusApp.Companion.BASE_URL
import xyz.theoye.hellobus.logic.model.VerifyCodeResponse
import com.google.gson.Gson
import retrofit2.converter.scalars.ScalarsConverterFactory
import android.R.attr.password
import retrofit2.Callback
import retrofit2.Response
import xyz.theoye.hellobus.logic.model.VerifyCodeRequest


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


interface CodeService{

//    @Headers("Content-type:application/json","charset=UTF-8")

    @POST("sms/v2/std/single_send")

    fun createCommit (@Body verifyCodeRequest: VerifyCodeRequest): Call<VerifyCodeResponse>

}



class ExampleUnitTest {



    @Test
    fun test(){
        //1. 创建Retrofit对象
        val retrofit =Retrofit.Builder()
            .baseUrl(BusApp.CODE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        //2. 创建service
        val service = retrofit.create(CodeService::class.java);


        //3. 准备数据
        val gson = Gson()

        val call = service.createCommit(VerifyCodeRequest(BusApp.CODE_APIKEY,"13305962916","sdkljf"))
        call.enqueue(object : Callback<VerifyCodeResponse> {
            override fun onResponse(call: Call<VerifyCodeResponse>, response: Response<VerifyCodeResponse>) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                print("SucessResult:" +response.body()?.result.toString())
            }

           override fun onFailure(call: Call<VerifyCodeResponse>, t: Throwable) {
                //for getting error in network put here Toast, so get the error on network
            }
        })




    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
