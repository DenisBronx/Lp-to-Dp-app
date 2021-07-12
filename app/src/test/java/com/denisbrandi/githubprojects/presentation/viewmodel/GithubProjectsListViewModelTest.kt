package com.denisbrandi.githubprojects.presentation.viewmodel

import com.denisbrandi.githubprojects.domain.model.*
import com.denisbrandi.githubprojects.domain.service.OrganisationValidator
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectsListViewModel.State.*
import com.denisbrandi.prelude.Answer
import com.denisbrandi.testutil.*
import org.junit.*

class GithubProjectsListViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private companion object {
        const val ORGANISATION = "square"
        val dummyProjects = listOf(GithubProject(id = "1"), GithubProject(id = "2"))
    }

    private val fakeGetProjectsForOrganisation = FakeGetProjectsForOrganisation()
    private val sut = GithubProjectsListViewModel(
        fakeGetProjectsForOrganisation::invoke,
        OrganisationValidator::isValidOrganisation
    )
    private val stateObserver = sut.state.test()

    @Test
    fun `loadProjects SHOULD emit Content state WHEN use case is successful`() {
        fakeGetProjectsForOrganisation.result = Answer.Success(dummyProjects)

        sut.loadProjects(ORGANISATION)

        stateObserver.assertValues(Idle, Loading, Content(dummyProjects))
    }

    @Test
    fun `loadProjects SHOULD emit Error state WHEN use case returns error`() {
        fakeGetProjectsForOrganisation.result = Answer.Error(GetProjectsError.NoProjectFound)

        sut.loadProjects(ORGANISATION)

        stateObserver.assertValues(Idle, Loading, Error(GetProjectsError.NoProjectFound))
    }

    @Test
    fun `loadProjects SHOULD emit InvalidInput WHEN organisation is empty`() {
        fakeGetProjectsForOrganisation.result = Answer.Error(GetProjectsError.NoProjectFound)

        sut.loadProjects("")

        stateObserver.assertValues(Idle, InvalidInput)
    }

    private class FakeGetProjectsForOrganisation {
        lateinit var result: Answer<List<GithubProject>, GetProjectsError>

        fun invoke(organisation: String): Answer<List<GithubProject>, GetProjectsError> {
            return stubOrThrow(
                isValidInvocation = organisation == ORGANISATION,
                result = result
            )
        }
    }

}