package tkuik.alexkarav.tkuikstudent.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tkuik.alexkarav.tkuikstudent.data.local.datastore.KeyStore
import tkuik.alexkarav.tkuikstudent.data.local.db.TimetableDatabase
import tkuik.alexkarav.tkuikstudent.data.remote.TimetableApi
import tkuik.alexkarav.tkuikstudent.domain.repo.TimetableRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideKeyStore(@ApplicationContext context: Context): KeyStore {
        return KeyStore(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TimetableDatabase {
        return Room.databaseBuilder(context, TimetableDatabase::class.java, "timetable_db")
            .addMigrations(TimetableDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(): TimetableApi {
        return Retrofit.Builder()
            .baseUrl(TimetableApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimetableApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimetableRepository(db: TimetableDatabase, keystore: KeyStore, api: TimetableApi): TimetableRepositoryImpl {
        return TimetableRepositoryImpl(db, api, keystore)
    }
}