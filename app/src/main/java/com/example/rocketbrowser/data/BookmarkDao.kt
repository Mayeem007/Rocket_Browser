package com.example.rocketbrowser.data

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks") fun getAll(): Flow<List<Bookmark>>
    @Insert fun insert(bookmark: Bookmark)
    @Delete fun delete(bookmark: Bookmark)
}
