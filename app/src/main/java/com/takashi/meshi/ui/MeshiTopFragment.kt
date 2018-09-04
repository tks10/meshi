package com.takashi.meshi.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dena2018.protein.onetimechat.R
import com.dena2018.protein.onetimechat.api.Api
import com.dena2018.protein.onetimechat.model.Token
import com.dena2018.protein.onetimechat.util.ApiErrorHandler
import com.dena2018.protein.onetimechat.util.ImageConverter
import com.dena2018.protein.onetimechat.util.UUIDManager
import com.takashi.meshi.R
import com.takashi.meshi.util.UUIDManager
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.FileDescriptor
import java.io.IOException


class SignUpFragment : Fragment() {

    private val um by lazy { UUIDManager(activity!!) }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.meshi_top_fragment, container, false)


        return view
    }
}
