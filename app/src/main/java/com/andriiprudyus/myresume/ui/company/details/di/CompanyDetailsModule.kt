package com.andriiprudyus.myresume.ui.company.details.di

import androidx.lifecycle.ViewModel
import com.andriiprudyus.myresume.di.ViewModelKey
import com.andriiprudyus.myresume.ui.company.details.viewModel.CompanyDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [CompanyDetailsComponent::class])
abstract class CompanyDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CompanyDetailsViewModel::class)
    abstract fun bindViewModel(viewModel: CompanyDetailsViewModel): ViewModel
}