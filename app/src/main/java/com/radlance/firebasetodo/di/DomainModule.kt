package com.radlance.firebasetodo.di

import com.radlance.firebasetodo.data.repository.AppRepositoryImpl
import com.radlance.firebasetodo.data.repository.AuthRepositoryImpl
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AppRepository
import com.radlance.firebasetodo.domain.repository.AuthRepository
import com.radlance.firebasetodo.presentation.auth.FireBaseUiMapper
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun provideAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun provideAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    fun provideAuthMapper(mapper: FireBaseUiMapper): FireBaseResult.Mapper<FireBaseUiState>

}