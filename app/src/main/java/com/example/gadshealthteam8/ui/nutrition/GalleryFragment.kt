package com.example.gadshealthteam8.ui.nutrition

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
import com.example.gadshealthteam8.Adapter.TipsItemAdapter
import com.example.gadshealthteam8.databinding.FragmentGalleryBinding
import com.example.gadshealthteam8.model.TipsModel
import com.example.multiversegads.ui.stories.GalleryViewModel
import com.google.firebase.firestore.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
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

        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

      //  val textView: TextView = binding.textGallery


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
        db.collection("HealthTips").document("Users").collection("Nutrition")
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