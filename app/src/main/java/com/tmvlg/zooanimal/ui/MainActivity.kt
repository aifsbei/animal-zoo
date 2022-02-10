package com.tmvlg.zooanimal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tmvlg.zooanimal.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, AnimalsFragment.newInstance())
            .commit()
    }
}