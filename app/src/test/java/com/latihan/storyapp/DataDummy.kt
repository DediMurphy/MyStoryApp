package com.latihan.storyapp

import com.latihan.storyapp.data.api.entity.StoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "photoUrl + $i",
                "name $i",
                "description $i",
                null,
                null,
                null
            )
            items.add(story)
        }
        return items
    }
}
