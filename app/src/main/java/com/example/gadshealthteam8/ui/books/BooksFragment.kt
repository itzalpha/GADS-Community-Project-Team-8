package com.example.gadshealthteam8.ui.books

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gadshealthteam8.Adapter.QuoteItemAdapter
import com.example.gadshealthteam8.databinding.BooksFragmentBinding
import com.example.gadshealthteam8.model.QuoteModel

import com.google.firebase.firestore.*

class BooksFragment : Fragment() {


    private  var _binding: BooksFragmentBinding? = null
    private lateinit var userArrayList: ArrayList<QuoteModel>
    private lateinit var myAdapter: QuoteItemAdapter
    private  var db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BooksFragmentBinding.inflate(inflater, container, false)
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