package com.example.githubkotlinrepos.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubkotlinrepos.R
import com.example.githubkotlinrepos.R.string
import com.example.githubkotlinrepos.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private val repoAdapter = RepoListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        reposList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.repos.observe(this, Observer { repos ->
            repos?.let {
                reposList.visibility = View.VISIBLE
                repoAdapter.updateRepos(it)
            }
        })

        viewModel.repoLoadError.observe(this, Observer { isError ->
            list_error.visibility = if (isError.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    reposList.visibility = View.GONE
                }
            }
        })
    }

    fun onAuthenticate(view: View) {
        val oauthUrl = getString(string.oauthUrl)
        val clientId = getString(string.clientId)
        val callbackUrl = getString(string.callbackUrl)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("$oauthUrl?client_id=$clientId&scope=repo&redirect_uri=$callbackUrl")
        )

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        val callbackUrl = getString(string.callbackUrl)
        if (uri != null && uri.toString().startsWith(callbackUrl)) {
            val code = uri.getQueryParameter("code")
            code?.let {
                val clientId = getString(string.clientId)
                val clientSecret = getString(string.clientSecret)
                viewModel.getToken(clientId, clientSecret, code)
            }
        }
    }
}