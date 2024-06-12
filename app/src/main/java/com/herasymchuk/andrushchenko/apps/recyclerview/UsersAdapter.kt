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
import com.herasymchuk.andrushchenko.apps.recyclerview.screens.UserListViewModel
import com.herasymchuk.andrushchenko.databinding.ItemUserBinding

interface UserActionListener {
    fun onUserMove(user: User, moveBy: Int)
    fun onUserDelete(user: User)
    fun onUserDetails(user: User)
    fun onUserFire(user: User)
}

class UsersDiffCallback(
    private val oldList: List<UserListViewModel.UserListItem>,
    private val newList: List<UserListViewModel.UserListItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].user.id == newList[newItemPosition].user.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}

class UsersAdapter(
    private val actionListener: UserActionListener,
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    private val users = UserList()

    fun updateUsers(newUsers: List<UserListViewModel.UserListItem>) {
        users.update(newUsers) { usersDiffCallback ->
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(usersDiffCallback)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class UserList {
        private var users: List<UserListViewModel.UserListItem> = emptyList()

        val size: Int
            get() = users.size

        val lastIndex: Int
            get() = users.lastIndex

        operator fun get(index: Int): UserListViewModel.UserListItem = users[index]

        fun indexOfFirst(predicate: (UserListViewModel.UserListItem) -> Boolean): Int = users.indexOfFirst(predicate)

        fun update(newUsers: List<UserListViewModel.UserListItem>, onUsersUpdated: (UsersDiffCallback) -> Unit) {
            val usersDiffCallback = UsersDiffCallback(users, newUsers)
            users = newUsers
            onUsersUpdated(usersDiffCallback)
        }
    }

    override fun onClick(v: View) {
        val userListItem: UserListViewModel.UserListItem = v.tag as UserListViewModel.UserListItem
        if (v.id == R.id.more_image_view_button) {
            showPopupMenu(v)
        } else {
            actionListener.onUserDetails(userListItem.user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userListItem: UserListViewModel.UserListItem = users[position]
        holder.itemView.tag = userListItem
        val user = userListItem.user

        with(holder.binding) {
            userNameTextView.text = user.name
            moreImageViewButton.tag = userListItem

            if (userListItem.inProgress) {
                moreImageViewButton.visibility = View.INVISIBLE
                itemProgressBar.visibility = View.VISIBLE
                root.setOnClickListener(null)
            } else {
                moreImageViewButton.visibility = View.VISIBLE
                itemProgressBar.visibility = View.GONE
                root.setOnClickListener(this@UsersAdapter)
            }

            userCompanyTextView.text = user.company
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

    private fun showPopupMenu(v: View) {
        val context: Context = v.context
        val userListItem: UserListViewModel.UserListItem = v.tag as UserListViewModel.UserListItem
        val user = userListItem.user
        val position = users.indexOfFirst { it.user.id == user.id }

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

    class UsersViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }
}
