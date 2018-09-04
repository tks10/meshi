package com.takashi.meshi.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takashi.meshi.R
import com.takashi.meshi.util.UUIDManager


class MeshiTopFragment : Fragment() {

    private val um by lazy { UUIDManager(activity!!) }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.meshi_top_fragment, container, false)


        return view
    }
}
