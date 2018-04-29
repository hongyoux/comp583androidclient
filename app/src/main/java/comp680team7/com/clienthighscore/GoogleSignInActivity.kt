package comp680team7.com.clienthighscore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import comp680team7.com.clienthighscore.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSignInActivity : AppCompatActivity(), View.OnClickListener {

    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if(lastSignedInAccount != null) {
            lastSignedInAccount.idToken?.let {
                authenticateWithBackend(it)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setColorScheme(SignInButton.COLOR_AUTO)

        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)
        findViewById<View>(R.id.goToMain).setOnClickListener {
            val i = Intent(this@GoogleSignInActivity, MainActivity::class.java)
            startActivity(i)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sign_in_button -> signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun authenticateWithBackend(token : String) {
        val attemptAuth = MainActivity.SERVICE.authenticate(token)
        attemptAuth.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                t?.printStackTrace()
                println("FAILURE!!")
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                println("SUCCESS!!")
                response?.let {
                    if(response.isSuccessful) {
                        if(response.body() != null) {
                            MainActivity.CURRENT_ACTIVE_USER = response.body()
                            val users = MainActivity.SERVICE.users
                            users.enqueue(object : Callback<List<User>> {
                                override fun onFailure(call: Call<List<User>>?, t: Throwable?) {

                                }

                                override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                                    response?.let {
                                        if(it.isSuccessful) {
                                            MainActivity.CACHE_USERS.clear()
                                            it.body()?.forEach({
                                                MainActivity.CACHE_USERS[it.id] = it
                                            })
                                        }
                                    }
                                }
                            })
                            updateUI(GoogleSignIn.getLastSignedInAccount(this@GoogleSignInActivity))
                            finish()
                        }
                    }
                }
            }
        })
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult<ApiException>(ApiException::class.java!!)
            val idToken = account.idToken

            idToken?.let {
                authenticateWithBackend(idToken)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {

            val signInToMain = Intent(this, GameListActivity::class.java)
            startActivity(signInToMain)
        } else {

            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        private val TAG = "SignInActivity"
        private val RC_SIGN_IN = 9001
    }
}
