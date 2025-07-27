package com.example.rocketbrowser.data

@Database(entities = [Bookmark::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
