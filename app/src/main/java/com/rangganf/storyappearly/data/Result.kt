package com.rangganf.storyappearly.data

/**
 * Sealed class yang merepresentasikan hasil operasi yang dapat berupa Success, Error, atau Loading.
 */
sealed class Result<out R> private constructor() {

    /**
     * Represents a successful result with data of type [T].
     * @param data Data of type [T].
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents an error result with an error message.
     * @param error Error message.
     */
    data class Error(val error: String) : Result<Nothing>()

    /**
     * Represents a loading result, indicating that the operation is still in progress.
     */
    object Loading : Result<Nothing>()
}
