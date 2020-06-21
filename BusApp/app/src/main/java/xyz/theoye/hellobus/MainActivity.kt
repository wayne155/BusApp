package xyz.theoye.hellobus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.theoye.hellobus.ui.login.UserLoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            val i = Intent(this, UserLoginActivity::class.java)
            startActivity(i)

//        signInButton.setOnClickListener {
//            val i = Intent(this, MapActivity::class.java)
//            startActivity(i)
//        }

    }
}
