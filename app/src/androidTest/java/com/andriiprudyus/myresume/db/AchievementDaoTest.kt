package com.andriiprudyus.myresume.db

import com.andriiprudyus.myresume.db.achievement.Achievement
import org.junit.Test

class AchievementDaoTest : BaseDaoTest() {

    @Test
    fun select() {
        val companyName = "eMagicOne"

        val expected = listOf(
            Achievement(
                "Android Developer / Team Lead",
                "Published Mobile Assistant for PrestaShop"
            ),
            Achievement(
                "Android Developer / Team Lead",
                "Published Mobile Assistant for WooCommerce"
            ),
            Achievement("Android Developer / Team Lead", "Published Mobile Assistant for Magento"),
            Achievement("Android Developer", "Developed Mobile Assistant for PrestaShop"),
            Achievement("Android Developer", "Developed Mobile Assistant for WooCommerce"),
            Achievement("Android Developer", "Developed Mobile Assistant for Magento")
        )

        db.achievementDao().select(companyName)
            .test()
            .assertValue { actual ->
                expected.size == actual.size && expected.containsAll(actual)
            }
            .assertComplete()
    }
}