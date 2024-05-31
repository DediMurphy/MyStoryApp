package com.latihan.storyapp.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.data.api.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)
            val story = responseData.listStory
            val dataStory = story.map {user ->
                user.toStoryEntity()
            }

            LoadResult.Page(
                data = dataStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}