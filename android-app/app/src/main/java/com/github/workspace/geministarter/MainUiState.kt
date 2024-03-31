package com.github.workspace.geministarter

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface MainUiState {

    /**
     * Empty state when the screen is first shown
     */
    data class Initial(
        val countries: List<Country> = Country.entries
    ) : MainUiState

    /**
     * Still loading
     */
    data object Loading : MainUiState

    /**
     * Text has been generated
     */
    data class Success(
        val country: Country,
        val capital: String
    ) : MainUiState

    /**
     * There was an error generating text
     */
    data class Error(
        val errorMessage: String
    ) : MainUiState
}