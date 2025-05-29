package com.example.fitup.hilt

import android.app.Application
import android.content.Context
import com.example.fitup.auth.AuthManager
import com.example.fitup.dataStore.UserLocalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@HiltAndroidApp
class FitUpApplication: Application()

@Module
@InstallIn(SingletonComponent::class) // makes it available app-wide
object FirebaseModule{

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth  = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseStorage(  )  = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton
    fun provideFirebaseDatabase(  )  = FirebaseDatabase.getInstance("https://katrinbe-af9ce-default-rtdb.europe-west1.firebasedatabase.app").reference

    @Provides
    @Singleton
    fun provideAuthManager() = AuthManager( provideFirebaseAuth(), provideFirebaseDatabase() )

    @Provides
    fun provideDatabaseManager() = DatabaseManager(provideFirebaseDatabase())

    @Provides
    @Singleton
    fun provideUserLocalData (@ApplicationContext context: Context): UserLocalData {
        return UserLocalData.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideStorageModel () = StorageViewModel(provideFirebaseStorage());

    @Provides
    @Singleton
    fun provideDbViewModel (@ApplicationContext context: Context) = DatabaseViewModel(
        provideDatabaseManager(),
        provideUserLocalData(context),
        provideAuthManager()
    )

    @Provides
    @Singleton
    fun provideLocalViewModel (@ApplicationContext context: Context) = LocalViewModel(
        provideUserLocalData(context),
        provideDbViewModel(context)
    )
}