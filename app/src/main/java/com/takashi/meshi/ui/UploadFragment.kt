package com.takashi.meshi.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.takashi.meshi.R
import com.takashi.meshi.api.Api
import com.takashi.meshi.model.MeshiUploader
import com.takashi.meshi.util.ApiErrorHandler
import com.takashi.meshi.util.ImageConverter
import com.takashi.meshi.util.UuidManager
import kotlinx.android.synthetic.main.upload_fragment.*
import kotlinx.android.synthetic.main.upload_fragment.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.FileDescriptor
import java.io.IOException
import android.view.animation.Animation
import android.view.animation.RotateAnimation


class UploadFragment : Fragment() {

    private val RESULT_PICK_IMAGEFILE = 1001
    private val RESULT_CAMERA = 1002
    private lateinit var imageView: ImageView
    private lateinit var imageUri: Uri
    private var originalName: String? = null
    private var imageChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.upload_fragment, container, false)
        val intentFuncLiteral = { _: View ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Filter to show only images, using the image MIME data type.
            // it would be "*/*".
            // intent.type = "*/*"

            startActivityForResult(intent, RESULT_CAMERA)
        }

        view.next_button.isEnabled = true
        view.next_button.setOnClickListener{
            val bitmapIcon = this@UploadFragment.imageView.drawable?.let {
                (it as BitmapDrawable).bitmap
            } ?: BitmapFactory.decodeResource(resources, R.drawable.onigiri)
            val imageBase64 = ImageConverter.convertToBase64(bitmapIcon)
            val meshiUploader = MeshiUploader(UuidManager(activity!!).getIdFromPreference()!!,
                    view.username_edit_text.text.toString(),
                    imageBase64)

            registMeshi(meshiUploader)
        }
        view.cancel_button.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        // In order to demonstrate catch the event of touching the back space, have to implement custom EditText.
        view.username_edit_text.setOnKeyListener { _, _, _ ->
            // Enable next button if the name filed has changed or is empty, otherwise disable it.
            view.next_button.isEnabled =
                    (username_edit_text.text.toString() != originalName && username_edit_text.text.toString() != "")
                    || imageChanged
            false
        }
        this.imageView = view.image_editing
        view.add_photo_button.setOnClickListener(intentFuncLiteral)
        view.image_editing.setOnClickListener(intentFuncLiteral)

        return view
    }

    private fun registMeshi(meshiUploader: MeshiUploader) {
        val rotate = RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        // animation時間 msec
        rotate.duration = 700
        // 繰り返し回数
        rotate.repeatCount = 1000
        // animationが終わったそのまま表示にする
        rotate.fillAfter = true

        launch (UI) {
            try {
                view?.next_button?.isEnabled = false
                view?.loadingImageView?.visibility = View.VISIBLE
                view?.loadingImageView?.startAnimation(rotate)
                view?.scrollView?.alpha = 0.15f
                Api.registMeshi(meshiUploader)
                activity!!.supportFragmentManager.popBackStack()
            } catch (t: Throwable) {
                t.printStackTrace()
                view?.next_button?.isEnabled = true
                view?.loadingImageView?.visibility = View.INVISIBLE
                view?.scrollView?.alpha = 1.0f
                view?.let {
                    ApiErrorHandler.map(view!!, t).post()
                }
            }
        }
    }

    // image added by user will notice the fragment by these member imageUri.
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if(resultData != null && resultData.data != null) {
                val pfDescriptor: ParcelFileDescriptor? = null
                try {
                    val uri: Uri = resultData.data
                    val pfDescriptor = activity!!.contentResolver.openFileDescriptor(uri, "r")
                    if (pfDescriptor != null) {
                        val fileDescriptor: FileDescriptor = pfDescriptor.fileDescriptor
                        val bmp: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                        pfDescriptor.close()
                        this.imageView.setImageBitmap(bmp)
                        this.imageUri = uri
                        add_photo_button.visibility = View.INVISIBLE
                        imageChanged = true
                        this.view?.next_button?.isEnabled = true
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        pfDescriptor?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        when(requestCode) {
            RESULT_CAMERA -> {
                var bitmap: Bitmap
                // cancelしたケースも含む
                resultData?.extras?.let {
                    bitmap = resultData.extras.get("data") as Bitmap
                    add_photo_button.visibility = View.INVISIBLE
                    this.imageView.setImageBitmap(bitmap)
                    this.view?.next_button?.isEnabled = true
                } ?: return
            }
        }
    }
}
