package com.andriiprudyus.myresume.ui.company.list.di

import dagger.Subcomponent

@Subcomponent
interface CompanyListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CompanyListComponent
    }
}