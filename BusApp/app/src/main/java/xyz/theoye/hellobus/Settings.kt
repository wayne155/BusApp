package xyz.theoye.hellobus

import xyz.theoye.common.Preferences

object Settings {

    //需要持久化的数据全部放在这里, 交给代理完成
    @JvmStatic
     var adminName:String by Preferences(AppContext, "adminName", "", "admin" )
    @JvmStatic

    var password:String by Preferences(AppContext, "password", "", "admin")

    @JvmStatic
    var userPhone:String by Preferences(AppContext, "userPhone", "", "user" )
    @JvmStatic
    var verifyCode:String by Preferences(AppContext, "verifyCode", "", "user")

    @JvmStatic
    var busStation:String by Preferences(AppContext, "busStation", "", "bus")
    @JvmStatic
    var busRoute:String by Preferences(AppContext, "busRoute", "", "bus")

    @JvmStatic
    var adminToken:String by Preferences(AppContext, "token", "", "admin")


    //剩余时间
//    var codeTime:String by Preferences(AppContext, "codeTime", "", "user")

}