package com.andriiprudyus.myresume.di

import android.app.Application
import com.andriiprudyus.myresume.db.DbMediator
import com.andriiprudyus.myresume.network.RestClientMediator
import com.andriiprudyus.myresume.sharedPreferences.CompanySharedPreferences
import com.andriiprudyus.myresume.ui.company.details.repository.CompanyDetailsRepository
import com.andriiprudyus.myresume.ui.company.list.repository.CompanyListRepository
import java.util.*

object Injector {

    lateinit var application: Application
    lateinit var companySharedPreferences: CompanySharedPreferences
    lateinit var dbMediator: DbMediator
    lateinit var restClientMediator: RestClientMediator
    lateinit var companyListRepository: CompanyListRepository
    lateinit var companyDetailsRepository: CompanyDetailsRepository

    fun init(application: Application) {
        this.application = application
        companySharedPreferences = CompanySharedPreferences(application)
        dbMediator = DbMediator(application)
        restClientMediator = RestClientMediator()
        companyDetailsRepository = CompanyDetailsRepository(dbMediator)

        companyListRepository = CompanyListRepository(
            restClientMediator,
            dbMediator,
            companySharedPreferences,
            Calendar.getInstance()
        )
    }
}