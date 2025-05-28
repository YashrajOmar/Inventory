package com.example.inventory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.example.inventory.databinding.ActivitySignUpBinding
import com.example.inventory.ui.navigation.NavigationDestination
import com.google.firebase.auth.FirebaseAuth

/*
class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase auth
        auth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            startActivity((Intent(this,LoginActivity::class.java)) )
            finish()
        }

        binding.registerButton.setOnClickListener {

            //get text form edit text field
            val email = binding.email.text.toString()
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()
            val repeatPasword = binding.rePassword.text.toString()

            //check if any field is blank
            if (email.isEmpty() || userName.isEmpty() || password.isEmpty() || repeatPasword.isEmpty()){
                Toast.makeText(this,"Please fill all the details", Toast.LENGTH_SHORT).show()
            } else if (password != repeatPasword){
                Toast.makeText(this, "Repeat Password must be same", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this,"Registration Successful", Toast.LENGTH_LONG).show()
                            startActivity((Intent(this,LoginActivity::class.java)) )
                            finish()
                        }
                        else {
                            Toast.makeText(this, "Registration Failed : ${task.exception?.message}",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}
 */
object SignupDestination : NavigationDestination {
    override val route = "Signup"
    override val titleRes = R.string.login_page
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(modifier: Modifier,
               navController: NavController
               , authViewModel: AuthViewModel){
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Signup Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.signup(email, password)
            }, enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Create account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Already have an account, Login")
        }

    }
}
