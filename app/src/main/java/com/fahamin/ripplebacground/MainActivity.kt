package com.fahamin.ripplebacground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.fahamin.ripplelibrary.RippleBackground

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var rbBackground1 = findViewById<RippleBackground>(R.id.rb1)

        var button = findViewById<Button>(R.id.btn_Start)
        button.setOnClickListener {
            if (rbBackground1.isRippleAnimationRunning) {
                rbBackground1.stopRippleAnimation();

            } else {
                rbBackground1.startRippleAnimation();

            }
        }
    }
}