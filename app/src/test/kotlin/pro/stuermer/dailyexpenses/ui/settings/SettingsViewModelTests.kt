package pro.stuermer.dailyexpenses.ui.settings

import io.mockk.coEvery
import io.mockk.mockk
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pro.stuermer.dailyexpenses.data.persistence.model.Sharing
import pro.stuermer.dailyexpenses.domain.usecase.CreateSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.GetSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.JoinSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.LeaveSharingUseCase
import pro.stuermer.dailyexpenses.domain.usecase.StartSyncUseCase

class SettingsViewModelTests {
    private val getSharingUseCase = mockk<GetSharingUseCase>()
    private val createSharingUseCase = mockk<CreateSharingUseCase>()
    private val joinSharingUseCase = mockk<JoinSharingUseCase>()
    private val leaveSharingUseCase = mockk<LeaveSharingUseCase>()
    private val startSyncUseCase = mockk<StartSyncUseCase>()

    @Test
    fun `sharing code should be returned in uistate`() = runTest {
        // given
        coEvery { getSharingUseCase.invoke() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        val viewModel = createTested()
        viewModel.loadSharing()

        // when
        val settingsUiState = viewModel.uiState.value

        // then
        assertEquals(expected = "ABC123", actual = settingsUiState.sharingCode)
    }

    @Test
    fun `join sharing with right code should succeed`() = runTest {
        // given
        coEvery { getSharingUseCase.invoke() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { joinSharingUseCase.invoke("ABC123") } answers {
            Result.success(true)
        }
        val viewModel = createTested()
        viewModel.loadSharing()

        // when
        viewModel.handleEvent(SettingsEvent.JoinShare(code = "ABC123"))

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "ABC123", actual = settingsUiState.sharingCode)
    }

    @Test
    fun `join sharing with wrong code should fail`() = runTest {
        // given
        coEvery { getSharingUseCase.invoke() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { joinSharingUseCase.invoke("ABC123") } answers {
            Result.failure(RuntimeException(""))
        }
        val viewModel = createTested()
        viewModel.loadSharing()

        // when
        viewModel.handleEvent(SettingsEvent.JoinShare(code = "ABC123"))

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "ABC123", actual = settingsUiState.sharingCode)
    }

    @Test
    fun `generate sharing code should succeed`() = runTest {
        // given
        coEvery { getSharingUseCase.invoke() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { joinSharingUseCase.invoke("ABC123") } answers {
            Result.failure(RuntimeException(""))
        }
        coEvery { createSharingUseCase.invoke() } answers {
            Result.success("ABC124")
        }
        val viewModel = createTested()
        viewModel.loadSharing()

        // when
        viewModel.handleEvent(SettingsEvent.GenerateShare)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "ABC123", actual = settingsUiState.sharingCode)
    }

    @Test
    fun `enter sharing code should update uistate`() = runTest {
        // given
        coEvery { getSharingUseCase.invoke() } answers {
            flowOf(listOf(Sharing(identifier = 1, code = "ABC123")))
        }
        coEvery { joinSharingUseCase.invoke("ABC123") } answers {
            Result.failure(RuntimeException(""))
        }
        coEvery { createSharingUseCase.invoke() } answers {
            Result.success("ABC124")
        }
        val viewModel = createTested()
        viewModel.loadSharing()

        // when
        viewModel.handleEvent(SettingsEvent.CodeChanged(code = "ABC"))

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "ABC", actual = settingsUiState.sharingCode)
    }

    private fun createTested(): SettingsViewModel {
        return SettingsViewModel(
            getSharingUseCase = getSharingUseCase,
            createSharingUseCase = createSharingUseCase,
            joinSharingUseCase = joinSharingUseCase,
            leaveSharingUseCase = leaveSharingUseCase,
            startSyncUseCase = startSyncUseCase,
        )
    }
}
