package com.denisbrandi.githubprojects.presentation.viewmodel

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.service.OrganisationValidator
import com.denisbrandi.githubprojects.domain.usecase.GetProjectsForOrganisation
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State.*
import com.denisbrandi.prelude.Answer
import com.denisbrandi.testutil.*
import org.junit.*

class GithubProjectsListViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val fakeGetProjectsForOrganisation = FakeGetProjectsForOrganisation()
    private val sut = GithubProjectsListViewModel(
        fakeGetProjectsForOrganisation,
        OrganisationValidator::isValidOrganisation
    )
    private val stateObserver = sut.state.test()

    @Test
    fun `EXPECT Content state WHEN use case is successful`() {
        fakeGetProjectsForOrganisation.result = Answer.Success(PROJECTS)

        sut.loadProjects(ORGANISATION)

        stateObserver.assertValues(Idle, Loading, Content(PROJECTS))
    }

    @Test
    fun `EXPECT Error state WHEN use case returns error`() {
        fakeGetProjectsForOrganisation.result = Answer.Error(GetProjectsError.NoProjectFound)

        sut.loadProjects(ORGANISATION)

        stateObserver.assertValues(Idle, Loading, Error(GetProjectsError.NoProjectFound))
    }

    @Test
    fun `EXPECT InvalidInput WHEN organisation is empty`() {
        fakeGetProjectsForOrganisation.result = Answer.Error(GetProjectsError.NoProjectFound)

        sut.loadProjects("")

        stateObserver.assertValues(Idle, InvalidInput)
    }

    private companion object {
        const val ORGANISATION = "square"
        val PROJECTS = listOf(GithubProject(id = "1"), GithubProject(id = "2"))
    }

    private class FakeGetProjectsForOrganisation : GetProjectsForOrganisation {
        lateinit var result: Answer<List<GithubProject>, GetProjectsError>

        override suspend fun invoke(organisation: String): Answer<List<GithubProject>, GetProjectsError> {
            return stubOrThrow(
                isValidInvocation = organisation == ORGANISATION,
                result = result
            )
        }
    }

}