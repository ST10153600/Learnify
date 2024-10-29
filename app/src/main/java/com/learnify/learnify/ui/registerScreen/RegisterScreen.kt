package com.learnify.learnify.ui.registerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.learnify.learnify.ui.loginScreen.PasswordField
import com.learnify.learnify.ui.loginScreen.UsernameField

@Composable
fun RegisterScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Register",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            UsernameField()
            Spacer(modifier = Modifier.height(16.dp))
            EmailField()
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField()
            Spacer(modifier = Modifier.height(16.dp))
            ConfirmPasswordField()
            Spacer(modifier = Modifier.height(32.dp))
            RegisterButton(navController)
        }
    }
}

@Composable
fun EmailField() {
    val email = remember { mutableStateOf("") }
    InputField(value = email, hint = "Email")
}

@Composable
fun ConfirmPasswordField() {
    val confirmPassword = remember { mutableStateOf("") }
    InputField(value = confirmPassword, hint = "Confirm Password", isPassword = true)
}

@Composable
fun RegisterButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("home_screen") },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = "Register", fontSize = 18.sp)
    }
}

@Composable
fun InputField(value: MutableState<String>, hint: String, isPassword: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (value.value.isEmpty()) {
            Text(text = hint, style = TextStyle(color = Color.DarkGray))
        }
        BasicTextField(
            value = value.value,
            onValueChange = { value.value = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
        )
    }
}
