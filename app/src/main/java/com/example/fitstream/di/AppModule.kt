package com.example.fitstream.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppContextEntryPoint {
    @ApplicationContext
    fun getContext(): Context
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun provideNotificationManager(
//        @ApplicationContext context: Context,
//        domofonCamerasRepository: DomofonRepository
//    ): NotificationManager {
//        return NotificationManagerImpl(context, domofonCamerasRepository)
//    }

//    @Provides
//    fun provideLogManager(
//        @ApplicationContext context: Context,
//        logDao: AppLogDao,
//        userInfoDao: UserInfoDao,
//        publicInfoDao: PublicInfoDao,
//        apiService: ApiService,
//        appAuth: AppAuth
//    ): LogManager {
//        return LogManagerImpl(
//            context = context,
//            dao = logDao,
//            userInfoDao = userInfoDao,
//            publicInfoDao = publicInfoDao,
//            api = apiService,
//            appAuth = appAuth
//        )
//    }

//    @Provides
//    fun provideRefreshManager(
//        @ApplicationContext context: Context,
//        appAuth: AppAuth,
//        dao: AppLogDao,
//        repository: Repository
//    ): RefreshManager {
//        return RefreshManagerImpl(
//            context = context,
//            appAuth = appAuth,
//            dao = dao,
//            repository = repository
//        )
//    }

//    @Provides
//    fun provideMyFirebaseMessagingService(
//        notificationManager: NotificationManager
//    ): MyFirebaseMessagingService {
//        return MyFirebaseMessagingService(notificationManager)
//    }

}