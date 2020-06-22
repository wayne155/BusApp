package xyz.theoye.hellobus.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.theoye.hellobus.logic.model.Location

class MapViewModel :ViewModel(){


    val location: MutableLiveData<Location> =  MutableLiveData<Location>(Location("", ""))
    

//    fun setLocation(Lag , lat)


}