package com.example.gadshealthteam8.ui.diseases

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.gadshealthteam8.Adapter.AdminTipsItemAdapter
import com.example.gadshealthteam8.Adapter.TipsItemAdapter
import com.example.gadshealthteam8.databinding.FragmentSlideshowAdminBinding
import com.example.gadshealthteam8.databinding.FragmentSlideshowBinding
import com.example.gadshealthteam8.model.TipsModel
import com.example.multiversegads.ui.speakers.SlideshowViewModel

import com.google.firebase.firestore.*

class AdminSlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowAdminBinding? = null
    private lateinit var userArrayList: ArrayList<TipsModel>
    private lateinit var myAdapter: AdminTipsItemAdapter
    private  var db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = context?.let { AdminTipsItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()




        return root
    }


    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("HealthTips").document("Users").collection("Diseases")
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
                    myAdapter.notifyDataSetChanged()


                }

            })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}