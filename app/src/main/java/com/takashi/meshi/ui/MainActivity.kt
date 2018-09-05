package com.takashi.meshi.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.takashi.meshi.R
import com.takashi.meshi.util.UuidManager


class MainActivity : AppCompatActivity(), NavigationHost {
    val uuidManager = UuidManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (uuidManager.getIdFromPreference() == null) {
                uuidManager.storeToPreference(UuidManager.generateUUID())
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, MeshiTopFragment())
                    .commit()
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
