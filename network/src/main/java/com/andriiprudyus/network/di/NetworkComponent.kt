package com.andriiprudyus.network.di

import dagger.Subcomponent

@Subcomponent
interface NetworkComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): NetworkComponent
    }
}