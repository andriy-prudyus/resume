package com.andriiprudyus.myresume.ui.company.details.di

import dagger.Subcomponent

@Subcomponent
interface CompanyDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CompanyDetailsComponent
    }
}