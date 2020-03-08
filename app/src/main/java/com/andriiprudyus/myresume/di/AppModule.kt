package com.andriiprudyus.myresume.di

import android.content.Context
import com.andriiprudyus.myresume.sharedPreferences.CompanySharedPreferences
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideCompanySharedPreferences(context: Context): CompanySharedPreferences {
        return CompanySharedPreferences(context)
    }

    @Provides
    fun provideCalendar(): Calendar = Calendar.getInstance()
}