package com.latihan.storyapp.data.api

import com.latihan.storyapp.data.api.entity.StoryEntity
import com.latihan.storyapp.data.api.response.ListStoryItem

fun ListStoryItem.toStoryEntity(): StoryEntity {
    return StoryEntity(
        id = id,
        name = name,
        photoUrl = photoUrl,
        description = description,
        createdAt = createdAt,
        lat = lat,
        lon = lon
    )
}