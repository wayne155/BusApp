package xyz.theoye.common


sealed class BooleanExt<out T>  //表状态, out T表示T用作返回值,协变

object  Otherwise : BooleanExt<Nothing>()  //Nothing是所有类的子类
class WithData<T>(val data:T):BooleanExt<T>()
    //这里的T是同一个类型, 同一个值


// T 相当于T: Any?  如果传Any都会报错的
fun<T> Boolean.yes(block: ()->T):BooleanExt<T>{
    // 这里的T都是fun<T>的T, 这个泛型在这里关键是用来代表返回值
    return when{
        this->{

            WithData( block())    //如果为真返回WithData对象, 供otherwise调用
        }
        else->{
            Otherwise   //如果为假yes方法返回Otherwise对象, 供otherwise调用
        }
    }
}

fun<T> Boolean.no(block: ()->T):BooleanExt<T>{
    // 这里的T都是fun<T>的T, 这个泛型在这里关键是用来代表返回值
    return when{

        this->{
            Otherwise
                //如果为假返回Otherwise对象, 供otherwise调用
        }
        else->{
            WithData( block())   //如果为真no方法返回WithData对象, 供otherwise调用
        }
    }
}

fun<T> BooleanExt<T>.otherwise(block: ()->T):T{
    return when(this){  //when this时kotlin 会把this自动转换类型
        is Otherwise -> block()//代表为假
        is WithData -> this.data //为真otherwise什么也不干
    }
}