package com.andriiprudyus.myresume.di

import androidx.lifecycle.ViewModelProvider
import com.andriiprudyus.myresume.base.viewModel.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}