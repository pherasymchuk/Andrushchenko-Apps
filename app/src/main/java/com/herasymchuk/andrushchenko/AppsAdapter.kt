package com.herasymchuk.andrushchenko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herasymchuk.andrushchenko.databinding.AppItemBinding
import com.herasymchuk.andrushchenko.model.AppItem

class AppsAdapter(private val apps: List<AppItem>) :
    RecyclerView.Adapter<AppsAdapter.AppsViewHolder>() {

    class AppsViewHolder(
        private val itemLayout: AppItemBinding,
    ) : RecyclerView.ViewHolder(itemLayout.root) {
        fun bind(appItem: AppItem) {
            itemLayout.appName.text = appItem.name
            itemLayout.btnOpenApp.setOnClickListener {
                appItem.onButtonClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder =
        AppsViewHolder(
            AppItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = apps.size

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        holder.bind(apps[position])
    }
}
