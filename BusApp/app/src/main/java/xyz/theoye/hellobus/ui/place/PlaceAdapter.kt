package xyz.theoye.hellobus.ui.place

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.model.LatLng
import kotlinx.android.synthetic.main.activity_map.*
import xyz.theoye.hellobus.MapActivity
import xyz.theoye.hellobus.R
import xyz.theoye.hellobus.logic.model.Place

class PlaceAdapter (private val fragment: Fragment, private val placeList: List<Place>):
    Adapter<PlaceAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.place_item , parent, false)

        val holder = ViewHolder(view)

        //设置监听事件
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            val place = placeList[position] //获取城市
            val activity = fragment.activity

            when(activity){
                is MapActivity -> {
                    activity.drawerLayout.closeDrawers()//关闭滑动栏
                Log.d("PlaceAdapter", "latitude${ place.location.lat.toDouble()}: altitude${place.location.lng.toDouble()}")
                    //更改地图位置
                    activity.mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(
                       LatLng( place.location.lat.toDouble(), place.location.lng.toDouble())
                    ))

                }
            }
        }


        return holder
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }
    override fun getItemCount( )= placeList.size

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address


    }

}


