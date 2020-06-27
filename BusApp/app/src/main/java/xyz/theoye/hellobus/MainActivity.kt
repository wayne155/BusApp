package xyz.theoye.hellobus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.theoye.hellobus.BusApp.Companion.ADMIN_LOGIN_TYPE
import xyz.theoye.hellobus.logic.model.AdminLoginRequest
import xyz.theoye.hellobus.logic.network.BusAppNetwork
import xyz.theoye.hellobus.ui.login.UserLoginActivity

class MainActivity : AppCompatActivity() {


    val LOG_TAG:String = "MainActivity_";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        adminUsernameEdittext.addTextChangedListener{

        }


    }
}
