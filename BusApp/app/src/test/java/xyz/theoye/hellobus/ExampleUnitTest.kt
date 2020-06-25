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
import com.google.gson.Gson
import retrofit2.converter.scalars.ScalarsConverterFactory
import android.R.attr.password
import com.google.gson.reflect.TypeToken
import retrofit2.Callback
import retrofit2.Response
import xyz.theoye.hellobus.logic.model.*
import xyz.theoye.hellobus.logic.network.LoginService
import java.util.ArrayList


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


interface CodeService{

    @Headers("Content-type:application/json")
    @POST("sms/v2/std/single_send")
    fun createCommit (@Body verifyCodeRequest: VerifyCodeRequest): Call<VerifyCodeResponse>

}



class ExampleUnitTest {
    @Test
    fun testLogin(){
        //1. 创建Retrofit对象
        val retrofit =Retrofit.Builder()
            .baseUrl(BusApp.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        //2. 创建service
        val service = retrofit.create(LoginService::class.java);
        print("service,service,service, ")


        //3. 准备数据
        val gson = Gson()

        val call = service.adminLogin( AdminLoginRequest("zhoujj",100,"zhoujjnb"))
        call.enqueue(object : Callback<AdminLoginResponse> {
            override fun onResponse(call: Call<AdminLoginResponse>, response: Response<AdminLoginResponse>) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                print("SucessResult:" +response.body()?.token.toString())
            }

            override fun onFailure(call: Call<AdminLoginResponse>, t: Throwable) {
                print("wrong,wrong,wrong, ")
                //for getting error in network put here Toast, so get the error on network
            }
        })



    }


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


    @Test
    fun test1(){
        val bus = BusStation("sldkjf", 32.34,123.0, "BeiJing")

        val gson = Gson()
        print("Json:"+gson.toJson(bus))

        val busStation = gson.fromJson("" , BusStation::class.java)
        print(busStation)
    }

    @Test
    fun test2(){
        val gson = Gson();
        val buss1 = BusStation("sldkjf", 32.34,123.0, "BeiJing")
        val bus2 = BusStation("士大夫", 22.34,113.0, "BeiJing2")
        val busses = ArrayList<BusStation>().apply { add(buss1) ; add(bus2)};

        val str = gson.toJson(busses)
        print("jsonRes: $str")

        val busStationType = object : TypeToken<ArrayList<BusStation>>() {
        }.type
        val busStations = gson.fromJson<ArrayList<BusStation>>(str, busStationType)


        val busRouteListType = object : TypeToken<ArrayList<BusRoute>>() {
        }.type
//        val busRoutes = gson.fromJson<ArrayList<BusRoute>>(Settings.busRoute, busRouteListType)
        print("UnserializedRes: $busStations")

    }

}
