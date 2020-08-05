package com.example.githubkotlinrepos.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubkotlinrepos.R
import com.example.githubkotlinrepos.model.GitHubRepo
import com.example.githubkotlinrepos.utils.loadImage
import kotlinx.android.synthetic.main.item_repo.view.*

class RepoListAdapter(private var repos: ArrayList<GitHubRepo>) :
    RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    fun updateRepos(newRepos: List<GitHubRepo>) {
        repos.clear()
        repos.addAll(newRepos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
    )

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position])
    }

    class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = view.imageView
        private val repoName = view.repoName
        private val userName = view.userName
        private val stars = view.stars
        private val forks = view.forks

        fun bind(repo: GitHubRepo) {
            imageView.loadImage(repo.owner.imageUrl)
            repoName.text = repo.name
            userName.text = repo.owner.login
            stars.text = repo.owner.starCount.toString()
            forks.text = repo.owner.forks.toString()
        }
    }
}