package com.takashi.meshi.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.takashi.meshi.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, MeshiTopFragment())
                    .commit()
        }
    }
}
