package com.freedom.loginwithgoogle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.freedom.loginwithgoogle.ui.theme.LoginWithGoogleTheme
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginWithGoogleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(

            modifier = modifier.padding(16.dp), onClick = {
                coroutineScope.launch {
                    val result: GetCredentialResponse? = buildCredentialRequest(context)
                    if (result != null) {
                        val googleIdTokenCredential: GoogleIdTokenCredential? = handleSignIn(result)
                        if (googleIdTokenCredential != null) {
                            Toast.makeText(
                                context,
                                "Email: ${googleIdTokenCredential.id}, " +
                                        "Display Name: ${googleIdTokenCredential.displayName}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context, "Error handling sign-in result.", Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context, "Error requesting credentials.", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }) {
            Text("Login with Google")
        }
    }
}

private suspend fun buildCredentialRequest(context: Context): GetCredentialResponse? {

    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        WEB_CLIENT_ID// TODO: Add your Web Client ID here
    ).build()

    val request: GetCredentialRequest =
        GetCredentialRequest.Builder().addCredentialOption(signInWithGoogleOption).build()

    val credentialManager: CredentialManager = CredentialManager.create(context)

    return try {
        credentialManager.getCredential(request = request, context = context)
    } catch (e: Exception) {
        Log.e("LoginWithGoogle", "Error requesting credentials: ${e.message}", e)
        null
    }
}

private fun handleSignIn(result: GetCredentialResponse): GoogleIdTokenCredential? {
    val credential: Credential = result.credential

    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val tokenCredential: GoogleIdTokenCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            return tokenCredential
        } catch (e: Exception) {
            Log.e(
                "LoginWithGoogle", "Error parsing Google ID token: ${e.message}", e
            )
        }
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    LoginWithGoogleTheme {
        Main()
    }
}