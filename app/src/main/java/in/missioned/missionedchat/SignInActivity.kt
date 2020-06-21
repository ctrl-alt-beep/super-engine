package `in`.missioned.missionedchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.foundation.Text
import androidx.ui.material.Snackbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_sign_in.*
import `in`.missioned.missionedchat.util.FirestoreUtil

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1
    private val signInProviders =
        listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.ic_check_circle)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        val progressBr = indeterminateProgressBar(this)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                progress_overlay.visibility = View.VISIBLE
                FirestoreUtil.initCurrentUserIfFirstTime {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                    progress_overlay.visibility = View.GONE
                }

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return
            when(response.error?.errorCode) {
                ErrorCodes.NO_NETWORK ->
                    Snackbar(
                        text = { Text(text= "No network") }
                    )
                ErrorCodes.UNKNOWN_ERROR ->
                    Snackbar(
                        text =  { Text(text = "Unknown error")}
                    )
            }
            }
        }

    }
}