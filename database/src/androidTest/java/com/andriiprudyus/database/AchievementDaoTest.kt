package com.andriiprudyus.database

import com.andriiprudyus.database.achievement.DbAchievement
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AchievementDaoTest : BaseDaoTest() {

    @Test
    fun select() {

        fun areEqual(expected: List<DbAchievement>, actual: List<DbAchievement>): Boolean {
            if (expected.size != actual.size) {
                return false
            }

            actual.forEach { a ->
                if (expected.find { b ->
                        a.companyName == b.companyName
                                && a.roleName == b.roleName
                                && a.achievementName == b.achievementName
                    } == null
                ) {
                    return false
                }
            }

            return true
        }

        val companyName = "eMagicOne"

        val expected = listOf(
            DbAchievement(
                companyName,
                "Android Developer / Team Lead",
                "Published Mobile Assistant for PrestaShop"
            ),
            DbAchievement(
                companyName,
                "Android Developer / Team Lead",
                "Published Mobile Assistant for WooCommerce"
            ),
            DbAchievement(
                companyName,
                "Android Developer / Team Lead",
                "Published Mobile Assistant for Magento"
            ),
            DbAchievement(
                companyName,
                "Android Developer",
                "Developed Mobile Assistant for PrestaShop"
            ),
            DbAchievement(
                companyName,
                "Android Developer",
                "Developed Mobile Assistant for WooCommerce"
            ),
            DbAchievement(
                companyName,
                "Android Developer",
                "Developed Mobile Assistant for Magento"
            )
        )

        runBlocking {
            assert(areEqual(expected, db.achievementDao().select(companyName)))
        }
    }
}