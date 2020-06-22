package xyz.theoye.common

import android.content.Context
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @param default 键不存在时的默认值
 * @param context 上下文, 这里我们指定为AppContext全局context
 * @param name 键
 * @param prefName SharedPreference的保存文件名
 *
 */
class Preferences<T> (val context: Context, val name:String , val default:T, val prefName : String = "default")
    : ReadWriteProperty<Any? , T> {


    private val prefs by lazy{
        //延迟初始化, 第一次使用时进行赋值, 其他时候不操作
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    }

    // 写属性时调用的方法, 这里我们希望写入到持久化SharedPreference中
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name , value)

    }
    // 读属性时调用的方法, 这里我们希望写入到持久化SharedPreference中
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name)
    }

    private fun findPreference(key:String):T
        =when(default){
            is Long -> prefs.getLong(key , default)
            is Int -> prefs.getInt(key , default)
            is String -> prefs.getString(key , default)
            else -> throw IllegalArgumentException("Key Not Exist!")
        } as T



    private  fun putPreference(key :String , value:T){
        with(prefs.edit()){
            when(value){
                is Long -> putLong(key , value)
                is String ->putString(key , value)
                is Int -> putInt(key , value)
                is Float->putFloat(key , value)
                is Boolean -> putBoolean(key , value)
                else -> throw IllegalArgumentException("Unsurported Type!");
            }.apply()
        }
    }

}