package me.ranieripieper.android.marvel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.ranieripieper.android.marvel.core.viewmodel.CoroutineContextProvider
import org.koin.dsl.module
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

val courotinesModule = module {
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return TestContextProvider()
    }

    single { provideCoroutineContextProvider() }
}

fun <T> T.toDeferred() = GlobalScope.async { this@toDeferred }

object MockitoHelper {

    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
        ArgumentCaptor.forClass(T::class.java)

    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    fun <T : Any> safeEq(value: T): T = ArgumentMatchers.eq(value) ?: value

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T

}

class TestObserver<T> : Observer<T> {
    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>()
    .also {
        observeForever(it)
    }