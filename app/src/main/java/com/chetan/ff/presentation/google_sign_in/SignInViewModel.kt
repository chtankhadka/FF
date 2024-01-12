package com.chetan.ff.presentation.google_sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.ff.data.model.SetGetGroupsName
import com.chetan.ff.data.model.SignInResult
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val preference: Preference,
    private val firestoreUseCases: FirestoreUseCases
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
            val mail = result.data?.userEmail?.split("@")?.get(0).toString()
            val groupname = mail.replace(Regex("[^A-Za-z0-9 ]"), "")
            preference.tableName = groupname
            preference.userName = result.data?.username
            preference.gmailProfile = result.data?.profilePictureUrl
            preference.groupName = groupname
            viewModelScope.launch {
                firestoreUseCases.setGroup(data = SetGetGroupsName(
                    groupName = groupname,
                    groupCreated = preference.userName?:"test",
                    createdTime = LocalDateTime.now().toString()
                ))
            }
        }
    }

    fun resetState(){
        _state.update {
            SignInState()
        }
    }
}
