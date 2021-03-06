package com.denisbrandi.githubprojects.presentation.viewmodel

import com.denisbrandi.githubprojects.domain.model.GithubProjectDetails
import com.denisbrandi.githubprojects.presentation.viewmodel.GithubProjectDetailsViewModel.State.*
import com.denisbrandi.prelude.Answer
import com.denisbrandi.testutil.*
import org.junit.*

class GithubProjectDetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val fakeGetProjectDetails = FakeGetProjectDetails()
    private val sut = GithubProjectDetailsViewModel(fakeGetProjectDetails::invoke)
    private val stateObserver = sut.state.test()

    @Test
    fun `EXPECT Content state WHEN use case is successful`() {
        fakeGetProjectDetails.result = Answer.Success(githubProjectDetails)

        sut.loadDetails(OWNER, REPOSITORY)

        stateObserver.assertValues(Idle, Loading, Content(githubProjectDetails))
    }

    @Test
    fun `EXPECT Error state WHEN use case is not successful`() {
        fakeGetProjectDetails.result = Answer.Error(Throwable())

        sut.loadDetails(OWNER, REPOSITORY)

        stateObserver.assertValues(Idle, Loading, Error)
    }

    private class FakeGetProjectDetails {
        lateinit var result: Answer<GithubProjectDetails, Throwable>

        fun invoke(owner: String, repository: String): Answer<GithubProjectDetails, Throwable> {
            return stubOrThrow(
                isValidInvocation = listOf(owner, repository) == listOf(OWNER, REPOSITORY),
                result = result
            )
        }
    }

    private companion object {
        const val OWNER = "square"
        const val REPOSITORY = "plastic"

        val githubProjectDetails = GithubProjectDetails(id = "1")
    }
}