package xyz.theoye.hellobus.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.theoye.hellobus.BusApp
import xyz.theoye.hellobus.MapActivity
import xyz.theoye.hellobus.R
import xyz.theoye.hellobus.Settings
import xyz.theoye.hellobus.logic.model.AdminLoginRequest
import xyz.theoye.hellobus.logic.model.VerifyCodeResponse
import xyz.theoye.hellobus.logic.network.BusAppNetwork
import java.lang.Exception
import java.lang.RuntimeException
import java.net.URLEncoder

class AdminLoginActivity :AppCompatActivity(){
    val LOG_TAG:String = "AdminLoginActivity_";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);  //设置管理员登陆


        //注册登陆事件
        adminLoginButton.setOnClickListener {

            val account = adminUsernameEdittext.text.toString()
            Log.d(LOG_TAG , "点集点集")
            val password = adminPasswordEdittext.text.toString()
            //如果没有输入用户名或者密码则提示后直接返回

            if(account.length<=5)
            {
                //显示
                Toast.makeText(this, "请输入大于5位的用户名以及密码", Toast.LENGTH_LONG ).show()
                return@setOnClickListener
            }
            if(password == ""){
                Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG ).show()
                return@setOnClickListener
            }

            GlobalScope.launch{
                val result = try {
                    val response = BusAppNetwork.adminLogin(AdminLoginRequest(account , BusApp.ADMIN_LOGIN_TYPE, password));

                    if(response.error_code != 0){
                        showLongToast("登陆失败,请检查用户名及密码")
                        return@launch
                    }

                    Settings.adminToken = response.token
                    showLongToast("登陆成功")

                    val intent = Intent(this@AdminLoginActivity, MapActivity::class.java);
                    startActivity(intent)
                }catch (e: Exception){
                    Result.failure<VerifyCodeResponse>(e)
                    Log.d(LOG_TAG, "Failure::")
                    e.printStackTrace()
                }

            }



        }

    }


    fun showLongToast(msg:String){
        runOnUiThread {
            Toast.makeText(this@AdminLoginActivity, msg, Toast.LENGTH_LONG ).show()
        }
    }
}