package com.example.githubkotlinrepos.model

import retrofit2.Response
import retrofit2.http.*

interface GitHubApi {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    suspend fun getAuthToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Response<GitHubToken>


    @Headers("Accept: application/vnd.github.v3+json")
    @GET("search/repositories?q=language:kotlin&sort=stars&order=desc&page=1&per_page=5")
    suspend fun getReposApi(): Response<List<GitHubRepo>>
}

