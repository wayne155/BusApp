package xyz.theoye.hellobus.logic.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.theoye.hellobus.BusApp
import xyz.theoye.hellobus.logic.model.PlaceResponse

interface PlaceService {

    @GET("v2/place?token=${BusApp.CITY_TOKEN}&lang=zh_CN")
    fun serachPlaces(@Query("query") query:String ): Call<PlaceResponse>
}