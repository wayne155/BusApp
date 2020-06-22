package xyz.theoye.hellobus.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.theoye.hellobus.BusApp

object ServiceCreator{

    private val codeRetrofit = Retrofit.Builder()
        .baseUrl(BusApp.CODE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val cityRetrofit = Retrofit.Builder()
        .baseUrl(BusApp.CITY_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BusApp.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //验证码请求retrofit建立
    fun <T> codeCreate(serviceClass:Class<T>) : T = codeRetrofit.create(serviceClass)
    inline fun <reified  T> codeCreate():T = codeCreate(T::class.java)


    //城市数据请求retrofit建立
    fun <T> cityCreate(serviceClass:Class<T>) : T = cityRetrofit.create(serviceClass)
    inline fun <reified  T> cityCreate():T = cityCreate(T::class.java)

    //基服务器请求retrofit建立
    fun <T> create(serviceClass:Class<T>) : T = retrofit.create(serviceClass)
    inline fun <reified  T> create():T = create(T::class.java)


}