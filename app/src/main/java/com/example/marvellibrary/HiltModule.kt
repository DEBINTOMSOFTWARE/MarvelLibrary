package com.example.marvellibrary

import android.content.Context
import androidx.room.Room
import com.example.marvellibrary.model.api.ApiService
import com.example.marvellibrary.model.api.MarvelApiRepo
import com.example.marvellibrary.model.connectivity.ConnectivityMonitor
import com.example.marvellibrary.model.db.CharacterDao
import com.example.marvellibrary.model.db.CollectionDb
import com.example.marvellibrary.model.db.CollectionDbRepo
import com.example.marvellibrary.model.db.CollectionDbRepoImpl
import com.example.marvellibrary.model.db.Constants.DB
import com.example.marvellibrary.model.db.NoteDao
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
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()

    @Provides
    fun provideCharacterDao(collectionDb: CollectionDb) = collectionDb.characterDao()

    @Provides
    fun provideNoteDao(collectionDb: CollectionDb) = collectionDb.noteDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectionDbRepo =
        CollectionDbRepoImpl(characterDao, noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)
}