package com.andriiprudyus.myresume.ui.company.list.di

import androidx.lifecycle.ViewModel
import com.andriiprudyus.myresume.di.ViewModelKey
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [CompanyListComponent::class])
abstract class CompanyListModule {

    @Binds
    @IntoMap
    @ViewModelKey(CompanyListViewModel::class)
    abstract fun bindViewModel(viewModel: CompanyListViewModel): ViewModel
}