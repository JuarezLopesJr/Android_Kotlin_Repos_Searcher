package com.example.githubkotlinrepos.model

import com.google.gson.annotations.SerializedName

data class GitHubRepo(
    val name: String?,
    val owner: GitHubUser
)