package xyz.theoye.hellobus.logic

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import xyz.theoye.hellobus.logic.model.Place
import xyz.theoye.hellobus.logic.network.BusAppNetwork
import java.lang.Exception
import java.lang.RuntimeException

object Repository {


    //利用liveData(Dispatchers.IO) 来创建协程
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        val result = try{
            val placeResponse = BusAppNetwork.searchPlaces(query)
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }

        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

}