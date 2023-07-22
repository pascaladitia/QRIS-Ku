package com.pascal.qris_ku.di

import android.content.Context
import androidx.room.Room
import com.pascal.qris_ku.common.utils.Constant
import com.pascal.qris_ku.data.LocalDatabase
import com.pascal.qris_ku.data.dao.LocalService
import com.pascal.qris_ku.data.datasource.LocalDatasource
import com.pascal.qris_ku.data.datasource.LocalDatasourceImpl
import com.pascal.qris_ku.data.repository.LocalRepository
import com.pascal.qris_ku.data.repository.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object MainModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object LocalModule {
        @Provides
        @Singleton
        fun providePokemonDatabase(
            @ApplicationContext context: Context
        ): LocalDatabase = Room
            .databaseBuilder(
                context,
                LocalDatabase::class.java,
                Constant.DB_NAME
            ).build()

        @Provides
        @Singleton
        fun providePokemonService(localDatabase: LocalDatabase) = localDatabase.localService()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object DataSourceModule {

        @Provides
        @Singleton
        fun provideLocalDatasource(
            localService: LocalService
        ): LocalDatasource {
            return LocalDatasourceImpl(localService)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {
        @Provides
        @Singleton
        fun provideLocalRepository(localDatasource: LocalDatasource): LocalRepository {
            return LocalRepositoryImpl(localDatasource)
        }
    }
}