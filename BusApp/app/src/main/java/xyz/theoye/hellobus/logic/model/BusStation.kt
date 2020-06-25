package xyz.theoye.hellobus.logic.model

import com.google.gson.Gson

data class BusStation(val name:String ,val altitude:Double, val latitude:Double ,  val city:String, var routesAttached:Int=0){
    fun clone():BusStation= BusStation(this.name , this.altitude, this.latitude, this.city, this.routesAttached)
}