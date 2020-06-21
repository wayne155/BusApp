package xyz.theoye.hellobus.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.theoye.hellobus.BusApp

object ServiceCreator{

    private val retrofit = Retrofit.Builder()
        .baseUrl(BusApp.CODE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass:Class<T>) : T = retrofit.create(serviceClass)

    inline fun <reified  T> create():T = create(T::class.java)


}