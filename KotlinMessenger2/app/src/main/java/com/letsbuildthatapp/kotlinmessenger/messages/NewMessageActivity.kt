package com.letsbuildthatapp.kotlinmessenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.letsbuildthatapp.kotlinmessenger.R
import com.letsbuildthatapp.kotlinmessenger.registration.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)


        supportActionBar?.title = "Select user"

 //       val adapter = GroupAdapter<ViewHolder>()
//
  //      adapter.add(UserItem())
    //    adapter.add(UserItem())
      //  adapter.add(UserItem())

     //   recycleview_newmessage.adapter = adapter

        fetchUsers()

    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){

       val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }

                }

                adapter.setOnItemClickListener{ item, view ->

                    val userItem = item as UserItem


                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)

                    startActivity(intent)

                    finish()
                }


                recycleview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.username_textview_new_message.text = user.usename


        Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.imageview_new_message)

    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message

    }
}

//this is super tedious

//class CustomAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {
  //  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    //}
//}