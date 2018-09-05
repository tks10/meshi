package com.takashi.meshi.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.takashi.meshi.R
import com.takashi.meshi.api.Api
import com.takashi.meshi.model.Meshi
import com.takashi.meshi.util.ApiErrorHandler
import com.takashi.meshi.util.GlideApp
import com.takashi.meshi.util.UuidManager
import com.takashi.meshi.util.getDateTime
import kotlinx.android.synthetic.main.meshi_top_fragment.view.*
import kotlinx.android.synthetic.main.upload_fragment.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.math.max


class MeshiTopFragment : Fragment() {

    private val meshies = mutableListOf<Meshi>()
    private val adapter: RecyclerView.Adapter<MeshiListAdapter.ViewHolder> by lazy {
        MeshiListAdapter(activity!!.applicationContext, meshies)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.meshi_top_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.meshi_list)

        view.postMeshiButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(UploadFragment(), true)
        }
        recyclerView.adapter = adapter

        meshies.add(Meshi("", "", "ONIGIRI", 1, 1536053423))
        meshies.add(Meshi("", "", "KARE-", 1, 1536032023))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1536026423))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1536022023))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1536021423))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1535992023))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1535966423))
        meshies.add(Meshi("", "", "OMUSUBI", 1, 1535962023))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1535926423))
        meshies.add(Meshi("", "", "YAKISOBA", 1, 1559012023))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1559900423))
        meshies.add(Meshi("", "", "ONIGIRI", 1, 1559900023))

        adapter.notifyDataSetChanged()

        return view
    }

    private fun uploadMeshies() {
        launch (UI) {
            try {
                val meshiContainer = Api.getMeshi(UuidManager(activity!!).getIdFromPreference()!!)
            } catch (t: Throwable) {
                t.printStackTrace()
                ApiErrorHandler.map(view!!, t).post()
            }
        }
    }
}

class MeshiListAdapter(val context: Context, private val meshies: List<Meshi>)
    : RecyclerView.Adapter<MeshiListAdapter.ViewHolder>() {
    private val BORDER_FACTOR = 0.02
    private val BORDER_MINIMUM = 16
    private val BORDER_THRESOHLD_SECOND = 5400L

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.meshi_icon)
        val memoTextView: TextView = view.findViewById(R.id.meshi_memo)
        val dateTimeTextView: TextView = view.findViewById(R.id.date_time_text_view)
        val border: View = view.findViewById(R.id.border)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.meshi_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meshi = meshies[position]
        val imageUrl =  "http://localhost:8000/"
        GlideApp.with(context)
                .load(R.drawable.onigiri)
                .fallback(R.drawable.onigiri)
                .into(holder.imageView)
        holder.memoTextView.text = meshi.memo
        holder.dateTimeTextView.text = getDateTime(meshi.created_at)

        if (position < meshies.size - 1) {
            holder.border.layoutParams.height = getDistanceBetween(meshies[position+1], meshi)
        } else {
            holder.border.layoutParams.height = 0
        }

    }

    override fun getItemCount() = meshies.size

    fun getDistanceBetween(from: Meshi, to: Meshi): Int {
        val filteredSecond = max((to.created_at - from.created_at) - BORDER_THRESOHLD_SECOND,
                                    BORDER_THRESOHLD_SECOND)
        val offsetedDistance = filteredSecond - BORDER_THRESOHLD_SECOND

        return ((offsetedDistance * BORDER_FACTOR) + BORDER_MINIMUM).toInt()
    }
}
