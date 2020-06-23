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
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
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

        import com.baidu.location.BDAbstractLocationListener;
        import com.baidu.location.BDLocation;
        import com.baidu.location.LocationClient;
        import com.baidu.location.LocationClientOption;
        import com.baidu.mapapi.CoordType;
        import com.baidu.mapapi.SDKInitializer;
        import com.baidu.mapapi.map.BaiduMap;
        import com.baidu.mapapi.map.BitmapDescriptor;
        import com.baidu.mapapi.map.BitmapDescriptorFactory;
        import com.baidu.mapapi.map.MapPoi;
        import com.baidu.mapapi.map.MapView;
        import com.baidu.mapapi.map.Marker;
        import com.baidu.mapapi.map.MarkerOptions;
        import com.baidu.mapapi.map.OverlayOptions;
        import com.baidu.mapapi.model.LatLng;

        import xyz.theoye.hellobus.Util.MyLocationListener;
        import xyz.theoye.hellobus.ui.map.MapViewModel;

public class MapActivity extends AppCompatActivity {


    public  MapViewModel viewModel = null;

    private MapView mMapView = null;
    public    BaiduMap mBaiduMap = null;



    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    public enum EditState{
        ADD_STATION , //编辑状态为添加站点
        DELETE_STATION,//删除站点
        ADD_BUSROUTE, //添加路线
        DELETE,
        DELETE_BUSROUTE, //删除路线
        NOTHING , //未选择任何状态
    }

    EditState editState = EditState.NOTHING;


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

//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //定义Maker坐标点
//                LatLng point = new LatLng(39.963175, 116.400244);
////构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.mipmap.ic_launcher);
////构建MarkerOption，用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions()
//                        .position(point)
//                        .icon(bitmap);
////在地图上添加Marker，并显示
//                mBaiduMap.addOverlay(option);
//            }
//        });

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

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
            }
        });

        viewModel.getEditState().observe(this, new Observer<EditState>() {
            @Override
            public void onChanged(EditState editState) {
                //如果是添加就可见
                if(editState==EditState.ADD_BUSROUTE || editState==EditState.ADD_STATION){
                    nameEditLayout.setVisibility(View.VISIBLE);
                }else if(editState == EditState.NOTHING || editState == EditState.DELETE){
                    nameEditLayout.setVisibility(View.GONE);
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





        //事件单击回调
        BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             *
             * @param point 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng point) {


                EditText editText = findViewById(R.id.editName);
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(viewModel.getEditState().getValue() == EditState.ADD_STATION){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bs);
//                    BitmapDescriptor bitmap = getBitmapDescriptor(R.mipmap.ic_launcher);

//构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
//在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                }else if (editState==EditState.NOTHING){

                }else if(editState == EditState.ADD_BUSROUTE){

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

    private void addBusStation(MapPoi mapPoi){
                        //定义Maker坐标点
                LatLng point = mapPoi.getPosition();
//构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_directions_bus_black_24dp);
//构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
//在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
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


    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
//    private BitmapDescriptor getBitmapDescriptor(int id) {
//        Drawable vectorDrawable =getApplicationContext().getDrawable(id);
//        int h = ((int) Utils.convertDpToPixel(42, getApplicationContext()));
//        int w = ((int) Utils.convertDpToPixel(25, getApplicationContext()));
//        vectorDrawable.setBounds(0, 0, w, h);
//        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bm);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bm);
//    }
}
