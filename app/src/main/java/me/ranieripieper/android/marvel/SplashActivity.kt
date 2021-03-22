package me.ranieripieper.android.marvel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import me.ranieripieper.android.marvel.feature.characters.view.CharacterActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            startActivity(Intent(baseContext, CharacterActivity::class.java))
            finish()
        }, 100)
    }
}