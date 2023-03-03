package ch.epfl.sdp.cook4me.ui

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import ch.epfl.sdp.cook4me.R
import ch.epfl.sdp.cook4me.ui.data.BoredUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class BoredActivity(
    // Check the response format of the response! See the documentation
    val activity: String,
)

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<BoredActivity>
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.boredapi.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val boredApi: BoredApi = retrofit.create(BoredApi::class.java)


class BoredViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BoredUiState())
    val uiState: StateFlow<BoredUiState> = _uiState.asStateFlow()

    init {
        changeActivity("If you're bored, tap the button to find your purpose in life")
    }

    fun generateActivity() {
        boredApi.getActivity().enqueue(object : Callback<BoredActivity> {
            override fun onResponse(call: Call<BoredActivity>, response: Response<BoredActivity>) {
                val act = response.body()?.activity
                if (act != null) {
                    changeActivity(act)
                }
            }

            override fun onFailure(call: Call<BoredActivity>, t: Throwable) {
                // TODO: Handle the error
            }
        })
    }

    private fun changeActivity(activity: String) {
        _uiState.update { currentState ->
            currentState.copy(activity = activity)
        }
    }
}