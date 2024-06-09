package com.herasymchuk.andrushchenko.apps.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.databinding.ItemUserBinding

interface UserActionListener {
    fun onUserMove(user: User, moveBy: Int)
    fun onUserDelete(user: User)
    fun onUserDetails(user: User)
    fun onUserFire(user: User)
}

class UsersDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}

class UsersAdapter(
    private val actionListener: UserActionListener,
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    private val users = UserList()

    fun updateUsers(newUsers: List<User>) {
        users.update(newUsers) { usersDiffCallback ->
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(usersDiffCallback)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class UserList {
        private var users: List<User> = emptyList()

        val size: Int
            get() = users.size

        val lastIndex: Int
            get() = users.lastIndex

        operator fun get(index: Int): User = users[index]

        fun indexOfFirst(predicate: (User) -> Boolean): Int = users.indexOfFirst(predicate)

        fun update(newUsers: List<User>, onUsersUpdated: (UsersDiffCallback) -> Unit) {
            val usersDiffCallback = UsersDiffCallback(users, newUsers)
            users = newUsers
            onUsersUpdated(usersDiffCallback)
        }
    }

    override fun onClick(v: View) {
        val user = v.tag as User
        if (v.id == R.id.more_image_view_button) {
            showPopupMenu(v)
        } else {
            actionListener.onUserDetails(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        holder.itemView.tag = user
        holder.bind(user)
    }

    private fun showPopupMenu(v: View) {
        val context: Context = v.context
        val user: User = v.tag as User
        val position = users.indexOfFirst { it.id == user.id }

        PopupMenu(context, v).apply {
            menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up))
                .isEnabled = (position > 0)
            menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down))
                .isEnabled = (position < users.lastIndex)
            menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))
            if (user.company.isNotBlank()) {
                menu.add(0, ID_FIRE, Menu.NONE, R.string.fire)
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    ID_MOVE_UP -> {
                        actionListener.onUserMove(user, -1)
                    }

                    ID_MOVE_DOWN -> {
                        actionListener.onUserMove(user, 1)
                    }

                    ID_REMOVE -> {
                        actionListener.onUserDelete(user)
                    }

                    ID_FIRE -> {
                        actionListener.onUserFire(user)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }.show()
    }

    class UsersViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                moreImageViewButton.tag = user
                userNameTextView.text = user.name
                userCompanyTextView.text = user.company.ifBlank {
                    itemView.context.getString(R.string.unemployed)
                }
                if (user.photo.isNotBlank()) {
                    Glide.with(photoImageView.context).load(user.photo).circleCrop()
                        .placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar)
                        .into(photoImageView)
                } else {
                    Glide.with(photoImageView.context).clear(photoImageView)
                    photoImageView.setImageResource(R.drawable.ic_user_avatar)
                }
            }
        }
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }
}
