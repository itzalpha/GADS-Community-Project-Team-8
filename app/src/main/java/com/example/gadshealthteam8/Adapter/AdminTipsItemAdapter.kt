package com.example.gadshealthteam8.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gadshealthteam8.R
import com.example.gadshealthteam8.model.TipsModel
import com.example.gadshealthteam8.admin.AdminTipsFullInfoActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.admin_item_tips_list.view.*
import java.util.*

class AdminTipsItemAdapter(val context: Context, private var userList : ArrayList<TipsModel>) :
    RecyclerView.Adapter<AdminTipsItemAdapter.ViewHolder>() {

    private lateinit var personName  : String
    private lateinit var personEmail : String
    private lateinit var personPhoto : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_tips_list ,parent , false  )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : TipsModel = userList[position]

        // TODO:Check SignUp Google Information
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (acct != null){
            personName = acct.displayName.toString()
            personEmail = acct.email.toString()
            personPhoto = acct.photoUrl.toString()
        }

        // TODO:Assign Value to xml object
        holder.quote.text = user.HealthTips

        holder.layout.setOnClickListener {
            val intent = Intent(context, AdminTipsFullInfoActivity::class.java)
            intent.putExtra("HealthTips", user.HealthTips)
            intent.putExtra("HealthTipsCategory", user.HealthTipsCategory)
            intent.putExtra("HealthTipsFullInfo" , user.HealthTipsFullInformation)
            intent.putExtra("docId" ,  user.HealthTipsDocumentId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val quote = view.item_tip_text
        val layout = view.tip_item_layout
    }

}