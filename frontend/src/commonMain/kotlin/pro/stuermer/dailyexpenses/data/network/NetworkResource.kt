package pro.stuermer.dailyexpenses.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface NetworkResource<out T> {
    data class Success<T>(val data: T) : NetworkResource<T>
    data class Error<T>(val message: Throwable? = null) : NetworkResource<T>
    object Loading : NetworkResource<Nothing>
}

fun <T> Flow<T>.asResource(): Flow<NetworkResource<T>> {
    return this.map<T, NetworkResource<T>> {
        NetworkResource.Success(it)
    }.onStart { emit(NetworkResource.Loading) }.catch { emit(NetworkResource.Error(it)) }
}
