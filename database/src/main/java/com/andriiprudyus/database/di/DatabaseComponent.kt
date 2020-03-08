package com.andriiprudyus.database.di

import dagger.Subcomponent

@Subcomponent
interface DatabaseComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DatabaseComponent
    }
}