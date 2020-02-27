package com.andriiprudyus.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.DateFormat

class DateUtilsTest {

    @Test
    fun formattedDate_timestampInvalid() {
        assertThat(formattedDate(0)).isEqualTo("")
    }

    @Test
    fun formattedDate_timestampValid() {
        assertThat(formattedDate(1582759727000)).isEqualTo("Feb 26, 2020")
    }

    @Test
    fun formattedDate_timestampValid_dateFormatFull() {
        assertThat(
            formattedDate(
                1582759727000,
                DateFormat.FULL
            )
        ).isEqualTo("Wednesday, February 26, 2020")
    }

    @Test
    fun formattedDate_timestampValid_dateFormatLong() {
        assertThat(formattedDate(1582759727000, DateFormat.LONG)).isEqualTo("February 26, 2020")
    }

    @Test
    fun formattedDate_timestampValid_dateFormatMedium() {
        assertThat(formattedDate(1582759727000, DateFormat.MEDIUM)).isEqualTo("Feb 26, 2020")
    }

    @Test
    fun formattedDate_timestampValid_dateFormatShort() {
        assertThat(formattedDate(1582759727000, DateFormat.SHORT)).isEqualTo("2/26/20")
    }
}