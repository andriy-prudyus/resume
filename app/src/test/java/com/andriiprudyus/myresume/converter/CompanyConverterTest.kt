package com.andriiprudyus.myresume.converter

import com.andriiprudyus.database.CompanyWithRelations
import com.andriiprudyus.database.RoleWithRelations
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.DbCompany
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.network.model.CompanyDto
import com.andriiprudyus.network.model.RoleDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CompanyConverterTest {

    @Test
    fun toCompaniesWithRelations() {
        val dtoList = listOf(
            CompanyDto(
                "eMagicOne",
                "Experienced mobile application developer with a lot of years of experience writing efficient code",
                "https://emagicone.com/i/logo_150dpi_cdr_1_site.png",
                listOf(
                    RoleDto(
                        "Android Developer / Team Lead",
                        1546300800000,
                        1577750400000,
                        listOf(
                            "Developing mobile apps for Android",
                            "Creating sprints and tasks",
                            "Estimating deadlines",
                            "Publishing apps on PlayStore"
                        ),
                        listOf(
                            "Published Mobile Assistant for PrestaShop",
                            "Published Mobile Assistant for WooCommerce",
                            "Published Mobile Assistant for Magento"
                        )
                    ),
                    RoleDto(
                        "Android Developer",
                        1503014400000,
                        1546214400000,
                        listOf(
                            "Developing mobile apps for Android",
                            "Writing tests"
                        ),
                        listOf(
                            "Developed Mobile Assistant for PrestaShop",
                            "Developed Mobile Assistant for WooCommerce",
                            "Developed Mobile Assistant for Magento"
                        )
                    )
                )
            ),
            CompanyDto(
                "Google",
                "Developed good mobile application, gained experience",
                "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                listOf(
                    RoleDto(
                        "Android Developer",
                        1464739200000,
                        1502928000000,
                        listOf(
                            "Developing mobile apps using Java",
                            "Refactoring",
                            "Learning new technologies"
                        ),
                        listOf(
                            "Learned MVVM",
                            "Learned MVC",
                            "Learned RxJava"
                        )
                    ),
                    RoleDto(
                        "Junior Android Developer",
                        1451606400000,
                        1464393600000,
                        listOf(
                            "Developing mobile apps using Java",
                            "Refactoring",
                            "Learning new technologies",
                            "Testing"
                        ),
                        listOf(
                            "Learned JUnit",
                            "Learned Espresso",
                            "Learned Android"
                        )
                    )
                )
            )
        )

        val expected = listOf(
            CompanyWithRelations(
                DbCompany(
                    "eMagicOne",
                    "Experienced mobile application developer with a lot of years of experience writing efficient code",
                    "https://emagicone.com/i/logo_150dpi_cdr_1_site.png"
                ),
                listOf(
                    RoleWithRelations(
                        DbRole("Android Developer / Team Lead", "eMagicOne", 1546300800000, 1577750400000),
                        listOf(
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Developing mobile apps for Android"
                            ),
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Creating sprints and tasks"
                            ),
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Estimating deadlines"
                            ),
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Publishing apps on PlayStore"
                            )
                        ),
                        listOf(
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Published Mobile Assistant for PrestaShop"
                            ),
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Published Mobile Assistant for WooCommerce"
                            ),
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer / Team Lead",
                                "Published Mobile Assistant for Magento"
                            )
                        )
                    ),
                    RoleWithRelations(
                        DbRole("Android Developer", "eMagicOne", 1503014400000, 1546214400000),
                        listOf(
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer",
                                "Developing mobile apps for Android"
                            ),
                            DbResponsibility(
                                "eMagicOne",
                                "Android Developer",
                                "Writing tests"
                            )
                        ),
                        listOf(
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer",
                                "Developed Mobile Assistant for PrestaShop"
                            ),
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer",
                                "Developed Mobile Assistant for WooCommerce"
                            ),
                            DbAchievement(
                                "eMagicOne",
                                "Android Developer",
                                "Developed Mobile Assistant for Magento"
                            )
                        )
                    )
                )
            ),
            CompanyWithRelations(
                DbCompany(
                    "Google",
                    "Developed good mobile application, gained experience",
                    "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                ),
                listOf(
                    RoleWithRelations(
                        DbRole("Android Developer", "Google", 1464739200000, 1502928000000),
                        listOf(
                            DbResponsibility(
                                "Google",
                                "Android Developer",
                                "Developing mobile apps using Java"
                            ),
                            DbResponsibility(
                                "Google",
                                "Android Developer",
                                "Refactoring"
                            ),
                            DbResponsibility(
                                "Google",
                                "Android Developer",
                                "Learning new technologies"
                            )
                        ),
                        listOf(
                            DbAchievement(
                                "Google",
                                "Android Developer",
                                "Learned MVVM"
                            ),
                            DbAchievement(
                                "Google",
                                "Android Developer",
                                "Learned MVC"
                            ),
                            DbAchievement(
                                "Google",
                                "Android Developer",
                                "Learned RxJava"
                            )
                        )
                    ),
                    RoleWithRelations(
                        DbRole("Junior Android Developer", "Google", 1451606400000, 1464393600000),
                        listOf(
                            DbResponsibility(
                                "Google",
                                "Junior Android Developer",
                                "Developing mobile apps using Java"
                            ),
                            DbResponsibility(
                                "Google",
                                "Junior Android Developer",
                                "Refactoring"
                            ),
                            DbResponsibility(
                                "Google",
                                "Junior Android Developer",
                                "Learning new technologies"
                            ),
                            DbResponsibility(
                                "Google",
                                "Junior Android Developer",
                                "Testing"
                            )
                        ),
                        listOf(
                            DbAchievement(
                                "Google",
                                "Junior Android Developer",
                                "Learned JUnit"
                            ),
                            DbAchievement(
                                "Google",
                                "Junior Android Developer",
                                "Learned Espresso"
                            ),
                            DbAchievement(
                                "Google",
                                "Junior Android Developer",
                                "Learned Android"
                            )
                        )
                    )
                )
            )
        )

        val actual = dtoList.toCompaniesWithRelations()

        assertThat(actual).containsExactlyElementsIn(expected).inOrder()
    }
}