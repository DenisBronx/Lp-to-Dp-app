package com.denisbrandi.githubprojects.domain.model

sealed class GetProjectsError {
    object NoProjectFound: GetProjectsError()
    object GenericError: GetProjectsError()
}