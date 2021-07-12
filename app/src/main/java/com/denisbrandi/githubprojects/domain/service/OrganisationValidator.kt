package com.denisbrandi.githubprojects.domain.service

object OrganisationValidator {

    fun isValidOrganisation(organisation: String): Boolean {
        return organisation.length >= 2
    }

}