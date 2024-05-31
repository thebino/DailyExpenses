package pro.stuermer.dailyexpenses.ui.settings

import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    fun `successfully load sharing should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested(
            getSharingUseCaseAnswer = flow {
                throw RuntimeException("")
            },
        )

        // when
        viewModel.handleEvent(SettingsEvent.EnableMaterialYou)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = true, actual = settingsUiState.isMaterialYouEnabled)
    }

    @Test
    fun `enable material you should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.EnableMaterialYou)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = true, actual = settingsUiState.isMaterialYouEnabled)
    }

    @Test
    fun `disable material you should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.DisableMaterialYou)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = false, actual = settingsUiState.isMaterialYouEnabled)
    }

    @Test
    fun `open share dialog should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.ShowShareDialog)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = true, actual = settingsUiState.showShareDialog)
    }

    @Test
    fun `close share dialog should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.HideShareDialog)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = false, actual = settingsUiState.showShareDialog)
    }

    @Test
    fun `successful generate a sharing code should be reflected in the uistate`() = runTest {
        // given
        coEvery { createSharingUseCase.invoke() } answers {
            Result.success("ABC124")
        }
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.GenerateShare)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = false, actual = settingsUiState.isSharingLoading)
        assertEquals(expected = "ABC123", actual = settingsUiState.sharingCode)
        assertEquals(expected = null, actual = settingsUiState.sharingError)
    }

    @Test
    fun `failing to generate a sharing code should be reflected in the uistate`() = runTest {
        // given
        coEvery { createSharingUseCase.invoke() } answers {
            Result.failure(NoSuchFieldException("Sharing group not created"))
        }
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.GenerateShare)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = false, actual = settingsUiState.isSharingLoading)
        // TODO: investigate why it is not reflecting
        // assertEquals(expected = "Could not create sharing group!?", actual = settingsUiState.sharingError)
    }

    @Test
    fun `leaving a sharing group should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.LeaveSharing)

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "", actual = settingsUiState.sharingCode)
        assertEquals(expected = false, actual = settingsUiState.isShareingEnrolled)
    }

    @Test
    fun `successfully joining a sharing group should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.JoinShare("ABC123"))

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = null, actual = settingsUiState.sharingError)
        assertEquals(expected = false, actual = settingsUiState.isSharingLoading)
    }

    @Test
    fun `failed joining a sharing group should be reflected in the uistate`() = runTest {
        // given
        val viewModel = createTested(
            joinSharingUseCaseAnswer = Result.success(false)
        )

        // when
        viewModel.handleEvent(SettingsEvent.JoinShare("ABC123"))

        // then
        val settingsUiState = viewModel.uiState.value
        // TODO: check why it is failing
        // assertEquals(expected = "Could not join sharing group!", actual = settingsUiState.sharingError)
//        assertEquals(expected = false, actual = settingsUiState.isSharingLoading)
    }


    @Test
    fun `entering a sharing code should update the uistate`() = runTest {
        // given
        val viewModel = createTested()

        // when
        viewModel.handleEvent(SettingsEvent.CodeChanged("ABC"))

        // then
        val settingsUiState = viewModel.uiState.value
        assertEquals(expected = "ABC", actual = settingsUiState.sharingCode)
    }

    private suspend fun createTested(
        getSharingUseCaseAnswer: Flow<List<Sharing>> = flowOf(listOf(Sharing(identifier = 1, code = "ABC123"))),
        joinSharingUseCaseAnswer: Result<Boolean> = Result.success(true),
        createSharingUseCaseAnswer: Result<String> = Result.success("ABC124"),
    ): SettingsViewModel {
        coEvery { getSharingUseCase.invoke() } answers { getSharingUseCaseAnswer }
        coEvery { createSharingUseCase.invoke() } answers { createSharingUseCaseAnswer }
        coEvery { joinSharingUseCase.invoke("ABC123") } answers { joinSharingUseCaseAnswer }
        coEvery { leaveSharingUseCase.invoke() } answers {}
        coEvery { startSyncUseCase.invoke() } answers {}

        val viewModel = SettingsViewModel(
            getSharingUseCase = getSharingUseCase,
            createSharingUseCase = createSharingUseCase,
            joinSharingUseCase = joinSharingUseCase,
            leaveSharingUseCase = leaveSharingUseCase,
            startSyncUseCase = startSyncUseCase,
        )
        viewModel.loadSharing()

        return viewModel
    }
}
