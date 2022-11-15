package com.example.gadshealthteam8.ui.speakers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gadshealthteam8.Adapter.QuoteItemAdapter
import com.example.gadshealthteam8.databinding.FragmentSlideshowBinding
import com.example.gadshealthteam8.model.QuoteModel
import com.example.multiversegads.ui.speakers.SlideshowViewModel

import com.google.firebase.firestore.*

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var userArrayList: ArrayList<QuoteModel>
    private lateinit var myAdapter: QuoteItemAdapter
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

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = context?.let { QuoteItemAdapter(it, userArrayList) }!!
        recyclerView.adapter = myAdapter
        EventChangeListener()




        return root
    }


    private fun filters(search: String) {
        val filterlist: java.util.ArrayList<QuoteModel> = java.util.ArrayList<QuoteModel>()
        for (  item in userArrayList) {
            if (item.MotivationCategory?.toLowerCase()
                    ?.contains(search.toLowerCase()) == true

            ) {
                filterlist.add(item)
            }
        }
        myAdapter.Filteredlist(filterlist)

    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("MotivationVerse").document("Users").collection("MotivationalQuote")
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }

                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(QuoteModel::class.java))
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