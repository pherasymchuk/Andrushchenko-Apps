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

/**
Callback for calculating the diff between two lists of [UserListViewModel.UserListItem] for use with [DiffUtil].

@param oldList The old list of users.
@param newList The new list of users.
 */
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

    /**
     * A class that represents a list of users.
     *
     * @property users A list of [UserListViewModel.UserListItem] objects.
     * @property size The number of users in the list.
     * @property lastIndex The index of the last user in the list.
     */
    class UserList {
        private var users: List<UserListViewModel.UserListItem> = emptyList()

        val size: Int
            get() = users.size

        val lastIndex: Int
            get() = users.lastIndex

        operator fun get(index: Int): UserListViewModel.UserListItem = users[index]

        fun indexOfFirst(predicate: (UserListViewModel.UserListItem) -> Boolean): Int = users.indexOfFirst(predicate)

        /**
         * @property onUsersUpdated A callback that is invoked when the users in the list are updated.
         */
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

    /**
    Binds the data to the [UsersViewHolder] at the specified position.

    - Sets the user's name, company, and photo.
    - Sets the tag of the `itemView` and `moreImageViewButton` to the corresponding [UserListViewModel.UserListItem].
    - Shows or hides the progress bar and `moreImageViewButton` depending on the `inProgress` state of the [UserListViewModel.UserListItem].
    - Sets the `root` view's click listener to the adapter if the item is not in progress.

    @param holder The [UsersViewHolder] to bind the data to.
    @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userListItem: UserListViewModel.UserListItem = users[position]
        holder.itemView.tag = userListItem
        val user = userListItem.user

        with(holder.binding) {
            userNameTextView.text = user.name
            userCompanyTextView.text = user.company

            Glide.with(photoImageView.context)
                .load(user.photo.ifBlank { null }) // Load image only if URL is available
                .circleCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .error(R.drawable.ic_user_avatar)
                .into(photoImageView)

            val isLoading = userListItem.inProgress
            moreImageViewButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            itemProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            root.setOnClickListener(if (isLoading) null else this@UsersAdapter)
            moreImageViewButton.tag = userListItem
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
