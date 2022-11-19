package com.example.gadshealthteam8.ui.lifestyles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.gadshealthteam8.Adapter.TipsItemAdapter
import com.example.gadshealthteam8.databinding.FragmentHomeBinding
import com.example.gadshealthteam8.model.TipsModel
import com.google.firebase.firestore.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var userArrayList: ArrayList<TipsModel>
    private lateinit var myAdapter: TipsItemAdapter
    private  var db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context ,  LinearLayoutManager.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        myAdapter = context?.let { TipsItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()



        return root
    }


    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("HealthTips").document("Users").collection("LifeStyles")
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
