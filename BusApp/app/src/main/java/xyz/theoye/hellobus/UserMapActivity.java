package xyz.theoye.hellobus;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.text.Layout;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.ColorInt;
        import androidx.annotation.DrawableRes;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.res.ResourcesCompat;
        import androidx.core.graphics.drawable.DrawableCompat;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;
        import xyz.theoye.hellobus.Settings;
        import com.baidu.location.BDAbstractLocationListener;
        import com.baidu.location.BDLocation;
        import com.baidu.location.LocationClient;
        import com.baidu.location.LocationClientOption;
        import com.baidu.mapapi.CoordType;
        import com.baidu.mapapi.SDKInitializer;
        import com.baidu.mapapi.map.BaiduMap;
        import com.baidu.mapapi.map.BitmapDescriptor;
        import com.baidu.mapapi.map.BitmapDescriptorFactory;
        import com.baidu.mapapi.map.InfoWindow;
        import com.baidu.mapapi.map.MapPoi;
        import com.baidu.mapapi.map.MapView;
        import com.baidu.mapapi.map.Marker;
        import com.baidu.mapapi.map.MarkerOptions;
        import com.baidu.mapapi.map.Overlay;
        import com.baidu.mapapi.map.OverlayOptions;
        import com.baidu.mapapi.map.Polyline;
        import com.baidu.mapapi.map.PolylineOptions;
        import com.baidu.mapapi.map.TextOptions;
        import com.baidu.mapapi.model.LatLng;
        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.lang.ref.WeakReference;
        import java.lang.reflect.Array;
        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Random;
        import java.util.Set;

        import xyz.theoye.hellobus.Util.MyLocationListener;
        import xyz.theoye.hellobus.logic.model.BusRoute;
        import xyz.theoye.hellobus.logic.model.BusStation;
        import xyz.theoye.hellobus.ui.map.MapViewModel;

public class MapActivity extends AppCompatActivity {


    private static WeakReference<Activity> mMapActivityRef;

    public WeakReference<Activity> getMapActivityRef(){return mMapActivityRef;}

    /***百度地图API所需变量***/
    public  MapViewModel viewModel = null;  //地图viewModel
    private MapView mMapView = null;
    public    BaiduMap mBaiduMap = null;

    public ArrayList<BusStation> busStations =new ArrayList<BusStation>() ;
    public ArrayList<BusRoute> busRoutes = new ArrayList<BusRoute>();
    public String currentCity = "北京";
    public String currentAddress = "北京";

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private ArrayList curRoute = new ArrayList<BusStation>();
    private ArrayList curTexts = new ArrayList<Overlay>();
    private int curStationIndex = 0;
    public enum EditState{
        ADD_STATION , //编辑状态为添加站点
        DELETE_STATION,//删除站点
        ADD_BUSROUTE, //添加路线
        DELETE,
        CHECK_INFO,
        DELETE_BUSROUTE, //删除路线
        NOTHING , //未选择任何状态
    }

    EditState editState = EditState.NOTHING;
    Gson gson =new Gson();


    public static void updateActivity(Activity activity) {
        mMapActivityRef = new WeakReference<Activity>(activity);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContex
        SDKInitializer.initialize(getApplicationContext());  //必须在setContentView之前
        setContentView(R.layout.activity_map);

        //获得viewModel
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        Button showMarker = findViewById(R.id.showMarker);
        LinearLayout nameEditLayout= findViewById(R.id.nameEditLayout);
        Button addStation  = findViewById(R.id.addStation);
        Button addRoute = findViewById(R.id.addRoute);
        Button deleteButton = findViewById(R.id.delBtn);
        EditText editText = findViewById(R.id.editName);
        Button finishEditButton = findViewById(R.id.finishEditButton);

        TextView editTextView = findViewById(R.id.editTextView);

        MapActivity.updateActivity(this);



        //定位

//        Button signInButton = findViewById(R.id.signInButton);


        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //显示卫星图层
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.showMapPoi(viewModel.getShowMarker().getValue());  //隐藏标注信息




        //****************获得保存的公交站点位置和路线*****************
        Type busStationListType = new TypeToken<ArrayList<BusStation>>(){}.getType();
        busStations = gson.fromJson(Settings.getBusStation(), busStationListType);
        Log.d("MapActivity bus", "busStations:" +Settings.getBusStation());
        if(busStations == null) busStations = new ArrayList<BusStation>() ;


        Type busRouteListType = new TypeToken<ArrayList<BusRoute>>(){}.getType();
        busRoutes = gson.fromJson(Settings.getBusRoute(), busRouteListType);
        if(busRoutes == null) busRoutes = new ArrayList<BusRoute>() ;
        //绘制公交站点
        drawBusStations(busStations);
        //绘制公交路线
        drawBusRoutes(busRoutes);
        //TODO()
        //注册点击事件
        Button navBtn = findViewById(R.id.navBtn);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        showMarker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.toggltShowMarker();
                viewModel.setEditState(EditState.CHECK_INFO);
            }
        });


        finishEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO()  完成编辑路径时要做的操作
                //必须输入路线名称
                if(editText.getText().toString().equals("")){
                    Toast.makeText(mMapActivityRef.get(), "请输入路线名称!!", Toast.LENGTH_LONG).show();
                    return ;
                }
                //如果只有一条路径就不管
                if(curRoute.size() ==0 || curRoute.size() ==1){
                    Toast.makeText(mMapActivityRef.get(), "路线中的站点应该至少有两个!!", Toast.LENGTH_LONG).show();
                    return ;
                }


                //添加到路线集
                BusRoute busRoute = new BusRoute( editText.getText().toString(), (List<BusStation>) curRoute.clone());
                busRoutes.add(busRoute);

                //绘制路线
                drawBusRoute(busRoute);

                //TODO 将路线中所有站点依附的加1
                busStationAttachedModify(busRoute, 1);


                //清空当前路线, 当前索引变为0
                curRoute.clear();
                curStationIndex = 0;
                //清空文本标记
                for (int j=  0; j < curTexts.size();j++)
                    ((Overlay) curTexts.get(j)).remove();

                curTexts.clear();

                //保存路线
                saveRoutes();
                //TODO()
            }
        });
        viewModel.getEditState().observe(this, new Observer<EditState>() {
            @Override
            public void onChanged(EditState editState) {

                //TODO()
                //如果是添加就可见

                switch (editState){

                    case ADD_BUSROUTE:
                        editTextView.setText("路线名称:");
                        nameEditLayout.setVisibility(View.VISIBLE);
                        finishEditButton.setVisibility(View.VISIBLE);
                        break;

                    case ADD_STATION:
                        editTextView.setText("站点名称:");
                        nameEditLayout.setVisibility(View.VISIBLE);
                        finishEditButton.setVisibility(View.GONE);
                        break;

                    case CHECK_INFO:
                        //TODO() 获取信息
                        nameEditLayout.setVisibility(View.GONE);
                        finishEditButton.setVisibility(View.GONE);

                        break;
                    case DELETE:
                        nameEditLayout.setVisibility(View.GONE);
                        finishEditButton.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }
        });
        viewModel.getShowMarker().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                {
                    showMarker.setBackgroundResource(R.drawable.ic_show_red_eye_black_24dp);
                    mBaiduMap.showMapPoi(true);  //隐藏标注信息
                }
                else
                {
                    showMarker.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    mBaiduMap.showMapPoi(false);  //隐藏标注信息
                }
            }
        });

        addStation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.setEditState(EditState.ADD_STATION);
            }
        });

        addRoute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.setEditState(EditState.ADD_BUSROUTE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.setEditState(EditState.DELETE);
            }
        });

        //设置滑动栏事件
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //关闭输入法
                 InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                 manager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        BaiduMap.OnPolylineClickListener polylineClickListener = new BaiduMap.OnPolylineClickListener() {
            /**
             * 地图 Polyline 覆盖物点击事件监听函数
             *
             * @param polyline 被点击的 polyline
             */
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                switch (viewModel.getEditState().getValue()){
                    case DELETE:
                        //弹出对话框
                        Log.d("onPlolhsdkljf", "polyline:");
                        String name = polyline.getExtraInfo().getString("name");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mMapActivityRef.get());
                        StringBuilder msg = new StringBuilder();
                        msg.append("你确定要").append("删除线路: [").append(name).append("]吗?");
                        builder.setMessage(msg.toString())
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    //点击成功的回调
                                    public void onClick(DialogInterface dialog, int id) {
                                        Bundle bundle = polyline.getExtraInfo();

                                        Toast.makeText(getApplicationContext(), "删除线路"+name+"成功", Toast.LENGTH_LONG).show();
                                        //删除路线
                                        int i = findBusRoute(bundle.getString("name"), bundle.getString("city"));
                                        busStationAttachedModify(busRoutes.get(i), -1); //依附路线-1
                                        busRoutes.remove(i);
                                        polyline.remove(); //删除该线
                                        saveRoutes(); //保存
                                    }
                                })
                                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //取消添加
                                        return;
                                    }
                                });
                        builder.create();
                        builder.show();
                        break;


                    case CHECK_INFO:
                        //TODO 查看折线数据

                        InfoWindow mInfoWindow = null;

//用来构造InfoWindow的Button
                        Button button = new Button(getApplicationContext());
                        button.setBackgroundResource(R.mipmap.rectangle_popup);
                        button.setText(polyline.getExtraInfo().getString("name"));
//构造InfoWindow
//point 描述的位置点
//-100 InfoWindow相对于point在y轴的偏移量
                        LatLng begin = polyline.getPoints().get(0) ;
                        LatLng end = polyline.getPoints().get(1) ;

                        LatLng center = new LatLng( (begin.latitude + end.latitude)/2 ,  (begin.longitude + end.longitude)/2 );

                        mInfoWindow = new InfoWindow(button, center, -10);

//使InfoWindow生效
                        mBaiduMap.showInfoWindow(mInfoWindow);

                        button.setOnClickListener(new View.OnClickListener(
                        ) {
                            @Override
                            public void onClick(View v) {
                                //吧按钮删除
                                ((ViewGroup)button.getParent()).removeView(button);
                            }
                        });

                        break;
                }
                return true;//是否捕获点击事件
            }
        };
//设置地图 Polyline 覆盖物点击事件监听
        mBaiduMap.setOnPolylineClickListener(polylineClickListener);

        BaiduMap.OnMarkerClickListener markerClickListener  = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();


                switch (viewModel.getEditState().getValue()){
                    case DELETE:
                        int index =findBusStation(bundle.getString("name"), bundle.getString("city"));
                        BusStation busStation = busStations.get(index);
                        //TODO() 若有依附线路则不可删除
                        if(busStation.getRoutesAttached()!=0){
                            Toast.makeText(getApplicationContext(), "站点["+marker.getTitle()+"]有路线依附", Toast.LENGTH_LONG).show();
                            return true;
                        }


                        //弹出对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(mMapActivityRef.get());
                        StringBuilder msg = new StringBuilder();
                        msg.append("你确定要").append("删除站点: [").append(marker.getTitle()).append("]吗?");
                        builder.setMessage(msg.toString())
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    //点击成功的回调
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(), "删除站点"+marker.getTitle()+"成功", Toast.LENGTH_LONG).show();
                                        busStations.remove(index);
                                        marker.remove();

                                        saveBusStation();
                                    }
                                })
                                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //取消添加
                                        return;
                                    }
                                });
                        builder.create();
                        builder.show();
                        break;


                    case CHECK_INFO:
                        InfoWindow mInfoWindow = null;

//用来构造InfoWindow的Button
                        Button button = new Button(getApplicationContext());
                        button.setBackgroundResource(R.mipmap.rectangle_popup);
                        button.setText(marker.getExtraInfo().getString("name"));
//构造InfoWindow
//point 描述的位置点
//-100 InfoWindow相对于point在y轴的偏移量
                        mInfoWindow = new InfoWindow(button, marker.getPosition(), -100);

//使InfoWindow生效
                        mBaiduMap.showInfoWindow(mInfoWindow);

                        button.setOnClickListener(new View.OnClickListener(
                        ) {
                            @Override
                            public void onClick(View v) {
                                //吧按钮删除
                                ((ViewGroup)button.getParent()).removeView(button);
                            }
                        });



//                        InfoWindow mInfoWindow = null;
//                        //用来构造InfoWindow
//                        BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.popup);
//
////响应点击的OnInfoWindowClickListener
//                        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
//                            @Override
//                            public void onInfoWindowClick() {
//                                Toast.makeText(mMapActivityRef.get(), "Click on InfoWindow", Toast.LENGTH_LONG).show();
//
//                            }
//                        };
//
////构造InfoWindow
////point 描述的位置点
////-100 InfoWindow相对于point在y轴的偏移量
//
//                         mInfoWindow = new InfoWindow(mBitmap, marker.getPosition(), -100, listener);
//                        mInfoWindow.setTag(marker.getTitle());
////使InfoWindow生效
//                        mBaiduMap.showInfoWindow(mInfoWindow);

                        break;
                    case ADD_BUSROUTE:
                        //TODO()  添加路径事件
                        OverlayOptions mTextOptions = new TextOptions()
                                .text(String.valueOf(curStationIndex + 1)) //文字内容
                                .bgColor(0xAAFFFF00) //背景色
                                .fontSize(60) //字号
                                .fontColor(0xFFFFFFFF) //文字颜色
                                .rotate(0) //旋转角度
                                .position(marker.getPosition());

                        //设置依附到了路线
                        Bundle bundle1 = marker.getExtraInfo();
                        bundle1.putBoolean("isAttatchedRoute", true);
                        marker.setExtraInfo(bundle1);

                        //添加到路径
                        int i =findBusStation(bundle1.getString("name"), bundle1.getString("city"));
                        curRoute.add( busStations.get(i).clone());
                        //保存Text, 以待删除
                        Overlay mText = mBaiduMap.addOverlay(mTextOptions);
                        curTexts.add(mText);


                        curStationIndex ++; //索引+1
                        break;
                }




                return true;//是否捕获点击事件
            }
        };
        mBaiduMap.setOnMarkerClickListener(markerClickListener);


        //事件单击回调
        BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             *
             * @param point 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng point) {


                switch (viewModel.getEditState().getValue()){
                    case ADD_STATION:
                        if(editText.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "请输入站点名称", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(findBusStation(editText.getText().toString(), currentCity)!=-1){
                            Toast.makeText(getApplicationContext(), "同一城市站点名不可相同", Toast.LENGTH_LONG).show();
                            return;
                        }

                            //弹出对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(mMapActivityRef.get());
                        StringBuilder msg = new StringBuilder();
                        msg.append("你确定要在").append(currentAddress).append("添加站点").append(editText.getText().toString());
                        builder.setMessage(msg.toString())
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    //点击成功的回调
                                    public void onClick(DialogInterface dialog, int id) {
                                        String name = editText.getText().toString();
                                        BusStation busStation = new BusStation(name, point.latitude, point.longitude, currentCity, 0);

                                        drawBusStation(busStation);
                                        addBusStation(busStation);
                                        editText.setText(""); //文本框置空

                                        saveBusStation();
                                    }
                                })
                                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //取消添加
                                        return;
                                    }
                                });
                        builder.create();
                        builder.show();



                        break;
                    case ADD_BUSROUTE:

                        break;

                    case DELETE:

                        break;
                }

            }
            /**
             * 地图内 Poi 单击事件回调函数
             * @param mapPoi 点击的 poi 信息
             */
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        };

        //设置单击的监听器
        mBaiduMap.setOnMapClickListener(listener);

    }


    private void busStationAttachedModify(BusRoute busRoute, int number) {
        for(BusStation busStation: busRoute.getRoute()){
            //依附路线加1
            int i = findBusStation(busStation.getName(), busStation.getCity());
            busStations.get(i).setRoutesAttached( busStation.getRoutesAttached() + number );
        }
    }

    private void drawBusRoute(BusRoute curRoute) {

        //准备点集
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0;  i< curRoute.getRoute().size();i++)
        {
            BusStation  busStation= curRoute.getRoute().get(i);
            points.add(new LatLng(busStation.getAltitude() , busStation.getLatitude()));
        }
        Random sRandom = new Random();
        //设置折线的属性
        Bundle bundle = new Bundle();

        bundle.putString("city", curRoute.getRoute().get(0).getCity());  //设置城市
        bundle.putString("name", curRoute.getName());

        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .extraInfo(bundle)
                .color(0xff000000 + 256 * 256 * sRandom.nextInt(256) + 256 * sRandom.nextInt(256)
                        + sRandom.nextInt(256))
                .points(points);
        //在地图上绘制折线
        //mPloyline 折线对象
        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
        //TODO  处理折线对象?
    }

    private void drawBusRoutes(ArrayList<BusRoute> busRoutes) {
        List<LatLng> points = new ArrayList<LatLng>();

        int i = 0;
        //TODO()
        for ( BusRoute busRoute: busRoutes){
            //绘制
            points.clear();
            i = 1;
            for( BusStation busStation : busRoute.getRoute()){
                //准备点集

                    points.add(new LatLng(busStation.getAltitude() , busStation.getLatitude()));

                OverlayOptions mTextOptions = new TextOptions()
                        .text(String.valueOf(i)) //文字内容
                        .bgColor(0xAAFFFF00) //背景色
                        .fontSize(60) //字号
                        .fontColor(0xFFFFFFFF) //文字颜色
                        .rotate(0) //旋转角度
                        .position(new LatLng(busStation.getAltitude() , busStation.getAltitude()));
                 i++;

                //TODO  绘制公交路线
            }
            drawBusRoute(busRoute);


        }

    }

    private void saveRoutes() {
        String json = gson.toJson(busRoutes);
        Settings.setBusRoute(json);
        Log.d("MapActivity","Routes:"+ Settings.getBusRoute());
    }

    private void addBusStation(BusStation busStation){
        busStations.add(busStation);
    }



    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        saveBusStation(); //保存公交站点数据
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明



            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            if(editState == EditState.ADD_STATION){
//                    String msg = "是否在"+mapPoi.getName()+"处"+"添加站点"+editText.getText().toString()+"?";

                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("")
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //确认添加
//                                    addBusStation(mapPoi);
                            }
                        })
                        .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //取消添加
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
            }




            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String adcode = location.getAdCode();    //获取adcode
            String town = location.getTown();    //获取乡镇信息
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveBusStation();
        //保存数据
    }

    private int findBusStation(String name, String city){
        for (int i = 0 ; i < busStations.size(); i++)
            if(name.equals(busStations.get(i).getName()) && city.equals(busStations.get(i).getCity())) return i;
        return -1;
    }


    private int findBusRoute(String name, String city){
        for (int i = 0 ; i < busRoutes.size(); i++)
            if(name.equals(busRoutes.get(i).getName()) && city.equals(busRoutes.get(i).getRoute().get(0).getCity())) return i;
        return -1;
    }
    private  Boolean checkBusStation(String name, String city){
        for (int i = 0 ; i < busStations.size(); i++)
        {
            if(name.equals(busStations.get(i).getName()) && city.equals(busStations.get(i).getCity())) return true;
            else return false;
        }
        return true;
    }

    private void saveBusStation(){
        String json = gson.toJson(busStations);

        Settings.setBusStation(json);

        Log.d("MapActivity","BusStatio:"+ Settings.getBusStation());

    }


    private void drawBusStation(BusStation busStation){
        if(busStation == null) return;
        Bundle bundle = new Bundle();
        //TODO()  保存依附的路径个数
        //保存索引
        bundle.putInt("index", busStations.size()-1);
        bundle.putBoolean("isRouteAttached", false);  //无路线依附
        bundle.putInt("routeAttached", 0);  //默认依附0个路线
        bundle.putString("city", busStation.getCity());
        bundle.putString("name", busStation.getName());
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bs);
        OverlayOptions option = new MarkerOptions()
                .position(new LatLng(busStation.getAltitude(),busStation.getLatitude()))   //绘制
                .clickable(true)
                .title(busStation.getName())  //设置标题
                .extraInfo(bundle)  //存放索引数据, 以便从中删除
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
    }
    //绘制BusStation
    private void drawBusStations(ArrayList<BusStation> arrayList){
        if(arrayList == null) return ;
        for(int i= 0 ; i < arrayList.size(); i ++){
            drawBusStation(arrayList.get(i));
        }
    }


}
