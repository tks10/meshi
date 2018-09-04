package com.takashi.meshi.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.takashi.meshi.R
import kotlinx.android.synthetic.main.upload_fragment.*
import kotlinx.android.synthetic.main.upload_fragment.view.*
import java.io.FileDescriptor
import java.io.IOException


class EditProfileFragment : Fragment() {

    private val RESULT_PICK_IMAGEFILE: Int = 1001
    private lateinit var imageView: ImageView
    private lateinit var imageUri: Uri
    private var originalName: String? = null
    private var imageChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.upload_fragment, container, false)
        val intentFuncLiteral = { _: View ->
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // it would be "*/*".
            intent.type = "*/*"

            startActivityForResult(intent, RESULT_PICK_IMAGEFILE)
        }

        view.next_button.setOnClickListener{
            // TODO
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
    }
}
