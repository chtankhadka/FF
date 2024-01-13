package com.chetan.ff.di

import android.app.Application
import com.chetan.ff.data.repositoryImpl.FDBRepositoryImpl
import com.chetan.ff.data.repositoryImpl.FirestoreRepositoryImpl
import com.chetan.ff.data.repositoryImpl.RealtimeRepositoryImpl
import com.chetan.ff.domain.repository.FDBRepository
import com.chetan.ff.domain.repository.FirestoreRepository
import com.chetan.ff.domain.repository.RealtimeRepository
import com.chetan.ff.domain.use_cases.fdb.FDBUseCases
import com.chetan.ff.domain.use_cases.fdb.InsertImage
import com.chetan.ff.domain.use_cases.firestore.DeleteRequestGroup
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.ff.domain.use_cases.firestore.GetGroups
import com.chetan.ff.domain.use_cases.firestore.GetRequestGroup
import com.chetan.ff.domain.use_cases.firestore.GetStatus
import com.chetan.ff.domain.use_cases.firestore.GetStories
import com.chetan.ff.domain.use_cases.firestore.RequestGroup
import com.chetan.ff.domain.use_cases.firestore.SetGroup
import com.chetan.ff.domain.use_cases.firestore.SetStories
import com.chetan.ff.domain.use_cases.firestore.UpdateCommentedUserInStories
import com.chetan.ff.domain.use_cases.firestore.UpdateStatus
import com.chetan.ff.domain.use_cases.realtime.GetItems
import com.chetan.ff.domain.use_cases.realtime.Insert
import com.chetan.ff.domain.use_cases.realtime.RealtimeUseCases
import com.chetan.orderdelivery.data.local.Preference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun providePreference(application: Application): Preference {
        return Preference(application)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageRepository(storage: FirebaseStorage, preference: Preference): FDBRepository {
        return FDBRepositoryImpl(storage,preference)
    }

    @Singleton
    @Provides
    fun provideStorageUseCases(repository: FDBRepository) =
        FDBUseCases(
            insertImage = InsertImage(repository = repository)
        )

    @Singleton
    @Provides
    fun provideFirebaseFirestoreRepository(
        firestore: FirebaseFirestore,
        preference: Preference
    ): FirestoreRepository {
        return FirestoreRepositoryImpl(firestore, preference)
    }

    @Singleton
    @Provides
    fun provideFirestoreUseCases(repository: FirestoreRepository) =
        FirestoreUseCases(
            updateStatus = UpdateStatus(frepository = repository),
            getStatus = GetStatus(frepository = repository),
            setStories = SetStories(frepository = repository),
            getStories = GetStories(frepository = repository),
            updateCommentedUserInStories = UpdateCommentedUserInStories(frepository = repository),
            getGroups = GetGroups(frepository = repository),
            setGroup = SetGroup(frepository = repository),
            requestGroup = RequestGroup(frepository = repository),
            getRequestGroup = GetRequestGroup(frepository = repository),
            deleteRequestGroup = DeleteRequestGroup(frepository = repository)
        )


    @Singleton
    @Provides
    fun provideFirebaseRealtime(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseRealtimeRepository(realtime: FirebaseDatabase,preference: Preference): RealtimeRepository {
        return RealtimeRepositoryImpl(realtime,preference)
    }

    @Singleton
    @Provides
    fun provideFirebaseRealtimeUseCases(repository: RealtimeRepository) =
        RealtimeUseCases(
            insert = Insert(realtimeRepository = repository),
            getItems = GetItems(realtimeRepository = repository),
        )
}