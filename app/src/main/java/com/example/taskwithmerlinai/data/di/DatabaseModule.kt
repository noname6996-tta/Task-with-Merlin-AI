package com.example.taskwithmerlinai.data.di

import android.content.Context
import com.example.taskwithmerlinai.data.db.AppDatabase
import com.example.taskwithmerlinai.data.db.TaskDao
import com.example.taskwithmerlinai.data.respository.TaskRepository
import com.example.taskwithmerlinai.data.respository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        dao: TaskDao,
    ): TaskRepositoryImpl {
        return TaskRepository(dao)
    }
}