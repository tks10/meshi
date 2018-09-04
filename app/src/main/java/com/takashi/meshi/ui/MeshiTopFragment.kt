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
import com.takashi.meshi.BuildConfig
import com.takashi.meshi.R
import com.takashi.meshi.model.Meshi
import com.takashi.meshi.util.GlideApp
import com.takashi.meshi.util.UUIDManager
import kotlinx.android.synthetic.main.meshi_top_fragment.view.*


class MeshiTopFragment : Fragment() {

    private val um by lazy { UUIDManager(activity!!) }
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
            (activity as NavigationHost).navigateTo(EditProfileFragment(), true)
        }
        recyclerView.adapter = adapter

        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "OMUSUBI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "OMUSUBI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "YAKISOBA", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        meshies.add(Meshi("", "", "ONIGIRI", 1, ""))
        adapter.notifyDataSetChanged()

        return view
    }
}

class MeshiListAdapter(val context: Context, private val meshies: List<Meshi>)
    : RecyclerView.Adapter<MeshiListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.meshi_icon)
        val memoTextView: TextView = view.findViewById(R.id.meshi_memo)
        // val userNameTextView: TextView = view.findViewById(R.id.talk_user_name)
        // val dateTextView: TextView = view.findViewById(R.id.talk_date)
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
        // holder.userNameTextView.text = meshi.category_id
    }

    override fun getItemCount() = meshies.size
}
