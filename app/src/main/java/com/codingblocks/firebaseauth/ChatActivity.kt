package com.codingblocks.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val user= FirebaseAuth.getInstance().currentUser
        val profileUpdate=UserProfileChangeRequest.Builder()
            .setDisplayName("Tanishq Sehgal").build()
        user?.updateProfile(profileUpdate)

        button2.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child("messages")
            .push().setValue(
            ChatMessege(
                input.text.toString(),
                FirebaseAuth.getInstance().currentUser?.displayName!!,
               Date().time
            )
        )
            input.setText("")
}

        val query = FirebaseDatabase.getInstance()
            .reference
            .child("messages")

        val options = FirebaseListOptions.Builder<ChatMessege>()
            .setLayout(R.layout.item_chat)
            .setQuery(query,ChatMessege::class.java)
            .build()


        val adapter= object : FirebaseListAdapter<ChatMessege>(options) {
            override fun populateView(v: View, model: ChatMessege, position: Int) {
                val message =v.findViewById<TextView>(R.id.message)
                val name=v.findViewById<TextView>(R.id.name)
                val time=v.findViewById<TextView>(R.id.time)
                message.text=model.message
                name.text=model.name
                time.text=android.text.format.DateFormat.format("dd-MMM-YY HH:mm:SS",model.time)
            }
        }
        adapter.startListening()
//        adapter.stopListening()
        messages.adapter=adapter

    }
    }



data class ChatMessege (
    val message:String,
    val name:String,
    val time:Long
) {
    constructor():this("","",0L)
}