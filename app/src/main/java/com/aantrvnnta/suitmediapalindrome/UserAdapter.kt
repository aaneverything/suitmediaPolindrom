package com.aantrvnnta.suitmediapalindrome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(
    private val users: MutableList<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun setData(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun addData(newUsers: List<User>) {
        val start = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(start, newUsers.size)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)

        fun bind(user: User) {
            tvName.text = "${user.first_name} ${user.last_name}"
            tvEmail.text = user.email
            Glide.with(itemView.context).load(user.avatar).into(ivAvatar)
            itemView.setOnClickListener { onUserClick(user) }
        }
    }
}

