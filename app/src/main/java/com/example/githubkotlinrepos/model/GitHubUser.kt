package com.example.githubkotlinrepos.model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val login: String?,

    @SerializedName("avatar_url")
    val imageUrl: String?,

    @SerializedName("stargazers_count")
    val starCount: Int?,

    val forks: Int?
)