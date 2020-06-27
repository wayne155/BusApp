package xyz.theoye.hellobus.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserLoginViewModel : ViewModel(){

    var codeTime:MutableLiveData<Int> = MutableLiveData(0)
    var codeText:MutableLiveData<String> = MutableLiveData("重新发送: 60秒")
    var codeStatus:MutableLiveData<Boolean> = MutableLiveData(true)
    fun codeTimeminus(){
        codeTime.value = codeTime.value?.minus(1)
    }
    fun codeTimeClear(){
        codeTime.value = 0
    }

    fun setSendable(){
        codeTime.value = 0
        codeStatus.value = true
    }
    fun setUnsendable(){
        codeTime.value = 60
        codeStatus.value = false
    }
}