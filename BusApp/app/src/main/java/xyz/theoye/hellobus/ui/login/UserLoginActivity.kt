package xyz.theoye.hellobus.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import xyz.theoye.common.CodeGenerator
import xyz.theoye.hellobus.logic.model.VerifyCodeRequest
import xyz.theoye.hellobus.logic.model.VerifyCodeResponse
import xyz.theoye.hellobus.logic.network.BusAppNetwork
import java.lang.Exception
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.theoye.hellobus.*
import java.lang.RuntimeException
import java.net.URLEncoder

class UserLoginActivity : AppCompatActivity() {

    lateinit  var viewModel: UserLoginViewModel   //viewModel

    
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(UserLoginViewModel::class.java)

        signInButton.setOnClickListener {
            if(verifyCodeText.text.length!=6){
            Toast.makeText(this, "请输入6位的验证码", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

            if(verifyCodeText.text.toString().equals(Settings.verifyCode)){
                Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG).show()
                val i = Intent(this, UserMapActivity::class.java)
                startActivity(i)
            }else{
                showToast("验证码错误")
            }
        }

        //注册监听事件, 当点击时改变文本倒计时
        viewModel.codeTime.observe(this , Observer { leftTime->
                when(leftTime){
                    0 -> {
                        sendCodeButton.setText("发送验证码")  //点击时就改
                    }
                    else -> {
                        sendCodeButton.setText("重试:$leftTime 秒")
                    }
                }
        })

        sendCodeButton .setOnClickListener{
            //检查文本
            if(phoneText.text.length!=11){
                Toast.makeText(this , "请输入正确的手机号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            //检查是否为可发送状态
            if(viewModel.codeStatus.value == true){
                //可以发送点击

                viewModel.setUnsendable()  //设置不可发送, time=60
                object : CountDownTimer(60000, 1000){
                    override fun onFinish() {
                        viewModel.codeTimeClear()
                        viewModel.setSendable()
                    }
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d("UserLoginActivity", "codeTime: " +viewModel.codeTime.value.toString())
                        viewModel.codeTimeminus()
                    }
                }.start()



                val gson = Gson()

                val verifyCode = CodeGenerator.generateCode(BusApp.CODE_LENGTH) //生成验证码
                val content:String = BusApp.CODE_TEXT_PREFIX +verifyCode +BusApp.CODE_TEXT_SUFFIX


                //content使用url编码方式
                val jsondata = gson.toJson(VerifyCodeRequest(BusApp.CODE_APIKEY,
                    URLEncoder.encode(content) , phoneText.text.toString() )) //


                //先写死..TODO()
                val verifyCodeRequest =VerifyCodeRequest(BusApp.CODE_APIKEY, phoneText.text.toString() ,URLEncoder.encode("验证码：$verifyCode，打死都不要告诉别人哦！", "GBK"))

                Log.d("UserLoginActivity","choded:"+ URLEncoder.encode("验证码：$verifyCode，打死都不要告诉别人哦！", "GBK"))

                //协程调用
                GlobalScope.launch{
                    val result = try {
                        val verifyCodeResponse= BusAppNetwork.requestVerifycode(verifyCodeRequest)

                        if(verifyCodeResponse.result.equals("0")){
                            //验证码发送成功, 存储起来

                            Settings.verifyCode = verifyCode.toString() //存储验证码
                            Result.success(verifyCode)
                        }else{
                            showToast("登陆失败,请检查输入信息");
                            Result.failure<Any>(RuntimeException("response result is ${verifyCodeResponse.result}"))
                        }
                    }catch (e: Exception){
                        Result.failure<VerifyCodeResponse>(e)
                        showToast("服务器响应错误");

                        e.printStackTrace()
                    }

                }



                Log.d("UserLoginActivity", "codeTime: $viewModel.codeTime")


            }

        }


        isAdminText.setOnClickListener {
            val intent = Intent(this , AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }


    fun showToast(msg:String ){
        runOnUiThread{
            Toast.makeText(this@UserLoginActivity, msg, Toast.LENGTH_LONG).show();
        }
    }





}
