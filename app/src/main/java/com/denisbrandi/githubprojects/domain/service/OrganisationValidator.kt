package com.denisbrandi.githubprojects.domain.service

internal object OrganisationValidator {

    fun isValidOrganisation(organisation: String): Boolean {
        return organisation.length >= 2
    }

}