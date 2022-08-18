package com.denisbrandi.githubprojects.presentation.view

import androidx.recyclerview.widget.DiffUtil
import com.denisbrandi.githubprojects.domain.model.GithubProject

internal class ItemDiffCallback : DiffUtil.ItemCallback<GithubProject>() {

    override fun areItemsTheSame(oldItem: GithubProject, newItem: GithubProject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GithubProject, newItem: GithubProject): Boolean {
        return oldItem == newItem
    }

}