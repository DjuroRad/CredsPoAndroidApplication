package com.example.credspoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
    }

    override fun onBackPressed() {
        //do nothing
    }
}