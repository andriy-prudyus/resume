package com.andriiprudyus.myresume.di

import android.content.Context
import com.andriiprudyus.database.di.DatabaseModule
import com.andriiprudyus.myresume.MainActivity
import com.andriiprudyus.myresume.ui.company.details.di.CompanyDetailsModule
import com.andriiprudyus.myresume.ui.company.list.di.CompanyListModule
import com.andriiprudyus.network.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        CompanyListModule::class,
        CompanyDetailsModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}