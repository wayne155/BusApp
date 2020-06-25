package xyz.theoye.hellobus

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import xyz.theoye.hellobus.logic.model.BusStation

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("xyz.theoye.hellobus", appContext.packageName)
    }

    @Test
    fun test(){
        val bus = BusStation("sldkjf", 32.34,123.0, "BeiJing")

        val gson = Gson()
        print("Json:"+gson.toJson(bus))

        val busStation = gson.fromJson("" , BusStation::class.java)
        print(busStation)
    }
}
