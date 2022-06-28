package com.keepin.android.di

import com.keepin.android.data.api.ArchiveService
import com.keepin.android.data.api.AuthService
import com.keepin.android.data.api.FindEmailService
import com.keepin.android.data.api.FindPasswordService
import com.keepin.android.data.api.FriendDetailService
import com.keepin.android.data.api.FriendUpdateService
import com.keepin.android.data.api.HomeService
import com.keepin.android.data.api.KeepInService
import com.keepin.android.data.api.MyPageService
import com.keepin.android.data.api.ProfileService
import com.keepin.android.data.api.ProfileUpdateService
import com.keepin.android.data.api.ReminderService
import com.keepin.android.data.api.ReminderUpdateService
import com.keepin.android.data.datasource.ArchiveDataSource
import com.keepin.android.data.datasource.ArchiveDataSourceImpl
import com.keepin.android.data.datasource.AuthDataSource
import com.keepin.android.data.datasource.AuthDataSourceImpl
import com.keepin.android.data.datasource.FindEmailDataSource
import com.keepin.android.data.datasource.FindEmailDataSourceImpl
import com.keepin.android.data.datasource.FindPasswordDataSource
import com.keepin.android.data.datasource.FindPasswordDataSourceImpl
import com.keepin.android.data.datasource.FriendDetailDataSource
import com.keepin.android.data.datasource.FriendDetailDataSourceImpl
import com.keepin.android.data.datasource.FriendUpdateDataSource
import com.keepin.android.data.datasource.FriendUpdateDataSourceImpl
import com.keepin.android.data.datasource.HomeDataSource
import com.keepin.android.data.datasource.HomeDataSourceImpl
import com.keepin.android.data.datasource.KeepInDataSource
import com.keepin.android.data.datasource.KeepInDataSourceImpl
import com.keepin.android.data.datasource.MyPageDataSource
import com.keepin.android.data.datasource.MyPageDataSourceImpl
import com.keepin.android.data.datasource.ProfileDataSource
import com.keepin.android.data.datasource.ProfileDataSourceImpl
import com.keepin.android.data.datasource.ProfileUpdateDataSource
import com.keepin.android.data.datasource.ProfileUpdateDataSourceImpl
import com.keepin.android.data.datasource.ReminderDataSource
import com.keepin.android.data.datasource.ReminderDataSourceImpl
import com.keepin.android.data.datasource.ReminderUpdateDataSource
import com.keepin.android.data.datasource.ReminderUpdateDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideAuthServiceImpl(authService: AuthService): AuthDataSource =
        AuthDataSourceImpl(authService)

    @Provides
    @Singleton
    fun provideFineEmailServiceImpl(findEmailService: FindEmailService): FindEmailDataSource =
        FindEmailDataSourceImpl(findEmailService)

    @Provides
    @Singleton
    fun provideHomeServiceImpl(homeService: HomeService): HomeDataSource =
        HomeDataSourceImpl(homeService)

    @Provides
    @Singleton
    fun provideMyPageServiceImpl(myPageService: MyPageService): MyPageDataSource =
        MyPageDataSourceImpl(myPageService)

    @Provides
    @Singleton
    fun provideFriendDetailServiceImpl(friendDetailService: FriendDetailService): FriendDetailDataSource =
        FriendDetailDataSourceImpl(friendDetailService)

    @Provides
    @Singleton
    fun provideFriendUpdateServiceImpl(friendUpdateService: FriendUpdateService): FriendUpdateDataSource =
        FriendUpdateDataSourceImpl(friendUpdateService)

    @Provides
    @Singleton
    fun provideProfileServiceImpl(profileService: ProfileService): ProfileDataSource =
        ProfileDataSourceImpl(profileService)

    @Provides
    @Singleton
    fun provideProfileUpdateServiceImpl(profileUpdateService: ProfileUpdateService): ProfileUpdateDataSource =
        ProfileUpdateDataSourceImpl(profileUpdateService)

    @Provides
    @Singleton
    fun provideReminderServiceImpl(reminderService: ReminderService): ReminderDataSource =
        ReminderDataSourceImpl(reminderService)

    @Provides
    @Singleton
    fun provideReminderUpdateServiceIpl(reminderUpdateService: ReminderUpdateService): ReminderUpdateDataSource =
        ReminderUpdateDataSourceImpl(reminderUpdateService)

    @Provides
    @Singleton
    fun provideKeepInServiceImpl(keepInService: KeepInService): KeepInDataSource =
        KeepInDataSourceImpl(keepInService)

    @Provides
    @Singleton
    fun provideArchiveServiceImpl(archiveService: ArchiveService): ArchiveDataSource =
        ArchiveDataSourceImpl(archiveService)

    @Provides
    @Singleton
    fun provideFindPasswordServiceImpl(findPasswordService: FindPasswordService): FindPasswordDataSource =
        FindPasswordDataSourceImpl(findPasswordService)
}
