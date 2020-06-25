package xyz.theoye.hellobus

import android.app.Application
import android.content.ContextWrapper

private lateinit var INSTANCE:Application

class BusApp:Application(){


    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    companion object{
        const val CITY_TOKEN = "zO2HZWVkAJtEhzTu"
        const val ADMIN_LOGIN_TYPE = 100
        const val CODE_APIKEY = "9821fcca86fd96b280ea7f87dbba701c"  //验证码apikey
        const val CODE_URL = "http://api01.monyun.cn:7901/"  //验证码服务器地址
        const val CODE_LENGTH = 6
        const val CITY_URL = "https://api.caiyunapp.com/" //获取城市信息服务器
        const val BASE_URL = "http://134.175.100.199:80/"   //本程序专属后台服务器
        const val CODE_TEXT_PREFIX = "验证码："
        const val CODE_TEXT_SUFFIX = "，打死都不要告诉别人哦！"

    }
}


object  AppContext:ContextWrapper(INSTANCE)
