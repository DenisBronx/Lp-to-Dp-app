package com.denisbrandi.githubprojects.presentation.view

import android.view.*
import androidx.recyclerview.widget.*
import com.denisbrandi.githubprojects.databinding.ProjectListItemBinding
import com.denisbrandi.githubprojects.domain.model.GithubProject

class GithubProjectsAdapter(
    private val onProjectClicked: (githubProject: GithubProject) -> Unit
) : ListAdapter<GithubProject, GithubProjectsAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProjectClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(
        private val binding: ProjectListItemBinding,
        private val onProjectClicked: (githubProject: GithubProject) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(githubProject: GithubProject) {
            binding.projectName.text = githubProject.name
            binding.projectDescription.text = githubProject.description
            binding.projectItemCardView.setOnClickListener { onProjectClicked(githubProject) }
        }
    }
}