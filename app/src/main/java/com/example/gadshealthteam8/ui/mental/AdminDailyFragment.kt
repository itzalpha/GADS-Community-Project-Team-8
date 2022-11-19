package com.example.gadshealthteam8.ui.mental

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.gadshealthteam8.Adapter.AdminTipsItemAdapter
import com.example.gadshealthteam8.Adapter.TipsItemAdapter
import com.example.gadshealthteam8.databinding.AdminDailyFragmentBinding
import com.example.gadshealthteam8.databinding.DailyFragmentBinding
import com.example.gadshealthteam8.model.TipsModel

import com.google.firebase.firestore.*
import kotlin.let


class AdminDailyFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDailyFragment()
    }
    private lateinit var userArrayList: ArrayList<TipsModel>
    private lateinit var myAdapter: AdminTipsItemAdapter
    private  var db = FirebaseFirestore.getInstance()
    private lateinit var viewModel: DailyViewModel
    private var _binding : AdminDailyFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AdminDailyFragmentBinding.inflate(inflater , container , false)
        val root : View = binding.root

        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()

        myAdapter = context?.let { AdminTipsItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("HealthTips").document("Users").collection("Mental")
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }

                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(TipsModel::class.java))
                            myAdapter.notifyDataSetChanged()
                        }
                        myAdapter.notifyDataSetChanged()
                    }



                }

            })

    }



}