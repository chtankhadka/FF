package com.chetan.ff.presentation.google_sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chetan.ff.R
import com.chetan.ff.presentation.dialogs.PrivacyPolicyDialog
import com.chetan.ff.utils.LoadLottieAnimation


@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {

    var isChecked by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    if (showDialog) {
        PrivacyPolicyDialog(onDismiss = {
            showDialog = it
        })
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.signInError, block = {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentScale = ContentScale.Crop,
                contentDescription = "")
            decorateText(text = "F", fontSize = 50.sp)
            decorateText(text = "f", fontSize = 30.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                    }
                )
                Text(
                    modifier = Modifier.clickable {
                        showDialog = true
                    },
                    text = "Terms and Conditions",
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )


            }
            Button(
                elevation = ButtonDefaults.buttonElevation(10.dp),
                onClick = { onSignInClick() },
                enabled = isChecked,

                ) {
                LoadLottieAnimation(modifier = Modifier.size(44.dp), image = R.raw.google)
                Text(text = "Sign in with Google")
            }

        }
    }


}
@Composable
fun decorateText(text: String, fontSize: TextUnit) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        style = LocalTextStyle.current.merge(
            TextStyle(
                fontSize = fontSize,
                brush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.error,
                        MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ),
                drawStyle = Stroke(
                    width = 8f, join = StrokeJoin.Round,
                    pathEffect = PathEffect.cornerPathEffect(fontSize.value)
                )
            )
        )
    )
}