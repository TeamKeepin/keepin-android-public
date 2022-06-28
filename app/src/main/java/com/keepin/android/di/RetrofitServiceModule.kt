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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideFindEmailService(retrofit: Retrofit): FindEmailService =
        retrofit.create(FindEmailService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun provideMyPageService(retrofit: Retrofit): MyPageService =
        retrofit.create(MyPageService::class.java)

    @Provides
    @Singleton
    fun provideFriendDetailService(retrofit: Retrofit): FriendDetailService =
        retrofit.create(FriendDetailService::class.java)

    @Provides
    @Singleton
    fun provideFriendUpdateService(retrofit: Retrofit): FriendUpdateService =
        retrofit.create(FriendUpdateService::class.java)

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideProfileUpdateService(retrofit: Retrofit): ProfileUpdateService =
        retrofit.create(ProfileUpdateService::class.java)

    @Provides
    @Singleton
    fun provideReminderService(retrofit: Retrofit): ReminderService =
        retrofit.create(ReminderService::class.java)

    @Provides
    @Singleton
    fun provideReminderUpdateService(retrofit: Retrofit): ReminderUpdateService =
        retrofit.create(ReminderUpdateService::class.java)

    @Provides
    @Singleton
    fun provideKeepInService(retrofit: Retrofit): KeepInService =
        retrofit.create(KeepInService::class.java)

    @Provides
    @Singleton
    fun provideArchiveService(retrofit: Retrofit): ArchiveService =
        retrofit.create(ArchiveService::class.java)

    @Provides
    @Singleton
    fun provideFindPasswordService(retrofit: Retrofit): FindPasswordService =
        retrofit.create(FindPasswordService::class.java)
}
