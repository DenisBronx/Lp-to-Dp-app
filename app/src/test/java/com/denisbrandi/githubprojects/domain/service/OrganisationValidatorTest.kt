package com.denisbrandi.githubprojects.domain.service

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OrganisationValidatorTest {

    private val sut = OrganisationValidator

    @Test
    fun `EXPECT true WHEN string has more than 2 chars`() {
        val result = sut.isValidOrganisation("squ")

        assertThat(result).isTrue()
    }

    @Test
    fun `EXPECT true WHEN string has at least 2 chars`() {
        val result = sut.isValidOrganisation("sq")

        assertThat(result).isTrue()
    }

    @Test
    fun `EXPECT false WHEN string has less than 2 chars`() {
        val result = sut.isValidOrganisation("s")

        assertThat(result).isFalse()
    }
}