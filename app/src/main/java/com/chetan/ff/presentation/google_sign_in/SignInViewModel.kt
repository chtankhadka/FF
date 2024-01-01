package com.chetan.ff.presentation.google_sign_in

import androidx.lifecycle.ViewModel
import com.chetan.ff.data.model.SignInResult
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val preference: Preference
): ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError =  result.errorMessage
            )
        }
        if (state.value.isSignInSuccessful){
            preference.tableName = result.data?.userEmail?.split("@")?.get(0)
            preference.userName = result.data?.username
            preference.gmailProfile = result.data?.profilePictureUrl
        }
    }

    fun resetState(){
        _state.update {
            SignInState()
        }
    }
}