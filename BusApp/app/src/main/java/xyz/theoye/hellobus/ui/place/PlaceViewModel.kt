package xyz.theoye.hellobus.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import xyz.theoye.hellobus.logic.Repository
import xyz.theoye.hellobus.logic.model.Place

class  PlaceViewModel:ViewModel(){
    private val searchLiveData = MutableLiveData<String>()


    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){
        query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value  = query
    }
}