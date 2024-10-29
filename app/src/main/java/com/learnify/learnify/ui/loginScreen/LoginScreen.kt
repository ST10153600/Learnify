package com.learnify.learnify.ui.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.learnify.learnify.R

@Composable
fun LoginScreen(navController: NavController) {
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
            AppLogo()
            Spacer(modifier = Modifier.height(32.dp))
            UsernameField()
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField()
            Spacer(modifier = Modifier.height(16.dp))
            ForgotPasswordLink()
            Spacer(modifier = Modifier.height(16.dp))
            RegisterText(navController)
            Spacer(modifier = Modifier.height(32.dp))
            ContinueButton(navController)
        }
    }
}

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "App Logo",
        modifier = Modifier.size(100.dp)
    )
    Text(
        text = "Quiz App",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = "Login to continue",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun UsernameField() {
    val username = remember { mutableStateOf("") }
    InputField(value = username, hint = "Username")
}

@Composable
fun PasswordField() {
    val password = remember { mutableStateOf("") }
    InputField(value = password, hint = "Password", isPassword = true)
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

@Composable
fun ForgotPasswordLink() {
    Text(
        text = "Forgot Password?",
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun RegisterText(navController: NavController) {
    Text(
        text = "Haven't registered? Register here.",
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { navController.navigate("register_screen") }
    )
}

@Composable
fun ContinueButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("home_screen") },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = "Continue", fontSize = 18.sp)
    }
}
