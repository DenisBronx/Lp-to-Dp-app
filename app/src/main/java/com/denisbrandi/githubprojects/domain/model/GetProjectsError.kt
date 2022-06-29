package com.denisbrandi.githubprojects.domain.model

internal sealed class GetProjectsError {
    object NoProjectFound: GetProjectsError()
    object GenericError: GetProjectsError()
}