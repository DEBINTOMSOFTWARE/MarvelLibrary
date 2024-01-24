package com.example.marvellibrary

import android.content.Context
import androidx.room.Room
import com.example.marvellibrary.model.api.ApiService
import com.example.marvellibrary.model.api.MarvelApiRepo
import com.example.marvellibrary.model.db.CharacterDao
import com.example.marvellibrary.model.db.ComicsDb
import com.example.marvellibrary.model.db.ComicsDbRepo
import com.example.marvellibrary.model.db.ComicsDbRepoImpl
import com.example.marvellibrary.model.db.Constants.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideComicsDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ComicsDb::class.java, DB).build()

    @Provides
    fun provideCharacterDao(comicsDb: ComicsDb) = comicsDb.characterDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao): ComicsDbRepo = ComicsDbRepoImpl(characterDao)
}