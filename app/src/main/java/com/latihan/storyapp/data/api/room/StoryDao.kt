package com.latihan.storyapp.data.api.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.data.api.response.StoryResponse

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()


}