package com.fs.fscomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class Splash : AppCompatActivity() {
    lateinit var image1 : ImageView
    lateinit var image2 : ImageView

    private val SPLASH_TIME_IMG:Long = 1000/3 // 1 sec
    private val SPLASH_TIME_OUT:Long = 7000/3 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        image1=findViewById (R.id.imgEdif1)
        image2=findViewById (R.id.imgEdif2)

      //  Handler().postDelayed(  { image2.setImageResource(R.mipmap.ic_hvac0_round) } , SPLASH_TIME_IMG)
      //  Handler().postDelayed(  { image1.setImageResource(R.mipmap.ic_hvac1_round) } , SPLASH_TIME_IMG*2)
       // Handler().postDelayed(  { image2.setImageResource(R.drawable.ic_com_smart_round) } , SPLASH_TIME_IMG*3)
       // Handler().postDelayed(  { image2.setImageResource(R.mipmap.ic_hvac2_round) } , SPLASH_TIME_IMG*4)
       // Handler().postDelayed(  { image1.setImageResource(R.mipmap.ic_hvac3_round) } , SPLASH_TIME_IMG*5)
        Handler().postDelayed(  { image2.setImageResource(R.mipmap.ic_launcher_logo) } , SPLASH_TIME_IMG*6)
        Handler().postDelayed(  { image1.setImageResource(R.mipmap.ic_launcher_logo) } , SPLASH_TIME_IMG*7)

        Handler().postDelayed(
            {   startActivity(Intent(this,MainActivity::class.java))
                finish()  }
            , SPLASH_TIME_OUT)
    }
}
