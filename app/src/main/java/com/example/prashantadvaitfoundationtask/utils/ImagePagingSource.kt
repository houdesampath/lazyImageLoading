package com.example.prashantadvaitfoundationtask.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ImagePagingSource(
    private val imageUrlList: List<String>
) : PagingSource<Int, String>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val position = params.key ?: 0
        val loadSize = params.loadSize

        return try {
            val start = position * loadSize
            val end = minOf(start + loadSize, imageUrlList.size)
            val data = imageUrlList.subList(start, end)

            LoadResult.Page(
                data = data,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (end == imageUrlList.size) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}