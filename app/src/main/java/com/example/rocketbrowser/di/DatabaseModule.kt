package com.example.rocketbrowser.di

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context) =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "rocket_db").build()

    @Provides @Singleton
    fun provideBookmarkDao(db: AppDatabase) = db.bookmarkDao()
}
