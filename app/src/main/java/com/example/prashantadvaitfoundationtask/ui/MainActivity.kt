package com.example.prashantadvaitfoundationtask.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.prashantadvaitfoundationtask.databinding.ActivityMainBinding
import com.example.prashantadvaitfoundationtask.ui.adapter.ImagePagingAdapter
import com.example.prashantadvaitfoundationtask.utils.Constants
import com.example.prashantadvaitfoundationtask.utils.Status
import com.example.prashantadvaitfoundationtask.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val binding by viewBinding(ActivityMainBinding::inflate)
    val mainViewModel : MainViewModel by viewModels()
    private lateinit var gridAdapter: ImagePagingAdapter
    private var responseList: PagingData<String> = PagingData.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel.fetchMainData()
        observeData()
    }



    private fun initUi() {
        val recyclerView = binding.gridView
        recyclerView.setHasFixedSize(true)
        recyclerView.isVerticalScrollBarEnabled = true
        val layoutManager = GridLayoutManager(this@MainActivity, Constants.GRID_COLUMNS)
        recyclerView.layoutManager = layoutManager
        gridAdapter = ImagePagingAdapter()
        recyclerView.adapter = gridAdapter
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.pager.collectLatest {
                gridAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            mainViewModel.mainState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.gridView.visibility = View.GONE

                        Timber.i("Loading...")
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.gridView.visibility = View.VISIBLE

                        it.data?.let { mainResponseList ->
                            val list: MutableList<String> = mainResponseList.map { res -> res.getThumbnailUrl() }.toMutableList()
                            mainViewModel.updateImageUrlList(list)
                            Timber.i("Received list.")
                            initUi()
                        } ?: run {
                                Timber.e("Error: Failed to fetch list.")
                            }
                    }
                    // error occurred status
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        binding.gridView.visibility = View.GONE

                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT)
                            .show()

                        Timber.e(it.message.toString())
                    }
                }
            }
        }
    }

}