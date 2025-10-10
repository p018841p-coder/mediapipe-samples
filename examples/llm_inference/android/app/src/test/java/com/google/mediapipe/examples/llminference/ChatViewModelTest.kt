package com.google.mediapipe.examples.llminference

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var name = TestName()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun sendMessage_withError_removesLoadingMessage() = runTest {
        // Given a mock InferenceModel that throws an exception
        val model = mock<InferenceModel> {
            on { uiState } doReturn UiState()
            on { generateResponseAsync(
                "hello",
                onPartialResponse = { _, _ -> },
            ) } doAnswer {
                throw IllegalStateException("test")
            }
        }
        val viewModel = ChatViewModel(model)

        // When a message is sent
        viewModel.sendMessage("hello")

        // Then the loading message should be removed and an error message should be added
        val messages = viewModel.uiState.value.messages
        assertThat(messages).hasSize(2)
        assertThat(messages[0].author).isEqualTo(USER_PREFIX)
        assertThat(messages[1].author).isEqualTo(MODEL_PREFIX)
        assertThat(messages[1].isLoading).isFalse()
        assertThat(messages[1].rawMessage).contains("test")
    }
}