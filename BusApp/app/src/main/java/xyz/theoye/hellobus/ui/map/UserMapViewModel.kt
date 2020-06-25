package xyz.theoye.hellobus.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baidu.mapsdkplatform.comapi.map.v
import xyz.theoye.hellobus.MapActivity
import xyz.theoye.hellobus.logic.model.BusStation
import xyz.theoye.hellobus.logic.model.Location

class MapViewModel :ViewModel(){


    var showMarker:MutableLiveData<Boolean> = MutableLiveData(true)

    var editState:MutableLiveData<MapActivity.EditState> = MutableLiveData(MapActivity.EditState.NOTHING)


    fun toggltShowMarker(){
        showMarker.value = !showMarker.value!!
    }

    fun setEditState(state : MapActivity.EditState){
        editState.value = state
    }

    val location: MutableLiveData<Location> =  MutableLiveData<Location>(Location("", ""))


//    fun setLocation(Lag , lat)


}