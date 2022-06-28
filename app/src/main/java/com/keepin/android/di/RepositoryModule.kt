package com.keepin.android.di

import com.keepin.android.data.datasource.ArchiveDataSourceImpl
import com.keepin.android.data.datasource.AuthDataSourceImpl
import com.keepin.android.data.datasource.FindEmailDataSourceImpl
import com.keepin.android.data.datasource.FindPasswordDataSourceImpl
import com.keepin.android.data.datasource.FriendUpdateDataSourceImpl
import com.keepin.android.data.datasource.HomeDataSourceImpl
import com.keepin.android.data.datasource.KeepInDataSourceImpl
import com.keepin.android.data.datasource.MyPageDataSourceImpl
import com.keepin.android.data.datasource.ProfileDataSourceImpl
import com.keepin.android.data.datasource.ProfileUpdateDataSourceImpl
import com.keepin.android.data.datasource.ReminderDataSourceImpl
import com.keepin.android.data.datasource.ReminderUpdateDataSourceImpl
import com.keepin.android.data.datasource.SharedPreferencesDataSourceImpl
import com.keepin.android.data.repository.ArchiveRepository
import com.keepin.android.data.repository.AuthRepository
import com.keepin.android.data.repository.FindEmailRepository
import com.keepin.android.data.repository.FindPasswordRepository
import com.keepin.android.data.repository.FriendUpdateRepository
import com.keepin.android.data.repository.HomeRepository
import com.keepin.android.data.repository.KeepInRepository
import com.keepin.android.data.repository.MyPageRepository
import com.keepin.android.data.repository.ProfileRepository
import com.keepin.android.data.repository.ProfileUpdateRepository
import com.keepin.android.data.repository.ReminderRepository
import com.keepin.android.data.repository.ReminderUpdateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSourceImpl: AuthDataSourceImpl,
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ) = AuthRepository(authDataSourceImpl, sharedPreferencesDataSourceImpl)

    @Provides
    @Singleton
    fun provideFindEmailRepository(
        findEmailDataSourceImpl: FindEmailDataSourceImpl
    ) = FindEmailRepository(findEmailDataSourceImpl)

    @Provides
    @Singleton
    fun provideHomeRepository(
        homeDataSourceImpl: HomeDataSourceImpl,
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ) = HomeRepository(homeDataSourceImpl, sharedPreferencesDataSourceImpl)

    @Provides
    @Singleton
    fun provideMyPageRepository(
        myPageDataSourceImpl: MyPageDataSourceImpl,
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ) = MyPageRepository(myPageDataSourceImpl, sharedPreferencesDataSourceImpl)

    @Provides
    @Singleton
    fun provideFriendUpdateRepository(friendUpdateDataSourceImpl: FriendUpdateDataSourceImpl) =
        FriendUpdateRepository(friendUpdateDataSourceImpl)

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileDataSourceImpl: ProfileDataSourceImpl,
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ) = ProfileRepository(profileDataSourceImpl, sharedPreferencesDataSourceImpl)

    @Provides
    @Singleton
    fun provideProfileUpdateRepository(
        profileUpdateDataSourceImpl: ProfileUpdateDataSourceImpl,
        sharedPreferencesDataSourceImpl: SharedPreferencesDataSourceImpl
    ) = ProfileUpdateRepository(profileUpdateDataSourceImpl, sharedPreferencesDataSourceImpl)

    @Provides
    @Singleton
    fun provideReminderRepository(
        reminderDataSourceImpl: ReminderDataSourceImpl
    ) = ReminderRepository(reminderDataSourceImpl)

    @Provides
    @Singleton
    fun provideReminderUpdateRepository(
        reminderUpdateDataSourceImpl: ReminderUpdateDataSourceImpl
    ) = ReminderUpdateRepository(reminderUpdateDataSourceImpl)

    @Provides
    @Singleton
    fun provideArchiveRepository(archiveDataSourceImpl: ArchiveDataSourceImpl) =
        ArchiveRepository(archiveDataSourceImpl)

    @Provides
    @Singleton
    fun keepInRepository(
        keepInDataSourceImpl: KeepInDataSourceImpl
    ) = KeepInRepository(keepInDataSourceImpl)

    @Provides
    @Singleton
    fun provideFindPasswordRepository(
        findPasswordDataSourceImpl: FindPasswordDataSourceImpl
    ) = FindPasswordRepository(findPasswordDataSourceImpl)
}
