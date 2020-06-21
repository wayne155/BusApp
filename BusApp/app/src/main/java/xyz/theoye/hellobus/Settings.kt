package xyz.theoye.hellobus

import xyz.theoye.common.Preferences

object Settings {
    //需要持久化的数据全部放在这里, 交给代理完成
    var adminName:String by Preferences(AppContext, "adminName", "", "admin" )
    var password:String by Preferences(AppContext, "password", "", "admin")

    var userPhone:String by Preferences(AppContext, "userPhone", "", "user" )
    var verifyCode:String by Preferences(AppContext, "verifyCode", "", "user")



}