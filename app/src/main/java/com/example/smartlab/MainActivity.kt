package com.example.smartlab

import PrimaryButton
import android.os.Bundle
import android.util.Range
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartlab.ui.theme.HeaderColor
import com.example.smartlab.ui.theme.SmartLabTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartLabTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("main") { LogInScreen(navController) }
        composable("prooveEmail") { ProoveEmail(onResendCode = {ResendCode()}, navController = navController) }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("main") { LogInScreen(navController) }
        composable("prooveEmail") { ProoveEmail(onResendCode = {ResendCode()}, navController = navController) }
    }
    SmartLabTheme {
        ProoveEmail(navController = navController, onResendCode = { ResendCode()})
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000) // Задержка 2 секунды
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF007AFF))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row() {
            Text(
                text = "Смартлаб",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.width(5.dp))
            Image(
                painter = painterResource(id = R.drawable.sqlitl), // Ссылка на векторное изображение
                contentDescription = "..",
                contentScale = ContentScale.Fit, // Масштабирование изображения
                modifier = Modifier.size(30.dp) // Размер изображения
            )

        }
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    val pagerState = rememberPagerState()

    val pages = listOf(
        PageData("Анализы", "Здесь вы можете увидеть свои анализы", R.mipmap.ill_foreground, R.drawable.dots1),
        PageData("Удобство", "Работа с врачами станет проще", R.mipmap.doc1_foreground, R.drawable.dots2),
        PageData("Мониторинг", "Следите за состоянием здоровья", R.mipmap.doc2_foreground, R.drawable.dots3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    //.align(Alignment.Start)
                    .padding(16.dp)
            ) {
                Text("Пропустить")
            }
            Image(
                painter = painterResource(id = R.drawable.sq), // Ссылка на векторное изображение
                contentDescription = "..",
                contentScale = ContentScale.Fit, // Масштабирование изображения
                modifier = Modifier
                    .size(150.dp) // Размер изображения
                    .padding(top = 20.dp)
            )

        }
        Row(horizontalArrangement = Arrangement.Center) {
            HorizontalPager(
            count = pages.size,
            state = pagerState,
            //modifier = Modifier.weight(1f),
        ) { page ->
            WelcomePage(pages[page])
        }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    //.align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                activeColor = Color.Blue
            ) }

    }
}

@Composable
fun WelcomePage(page: PageData) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(page.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HeaderColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(page.description, fontSize = 16.sp, color = Color.DarkGray)
            Image(
                painter = painterResource(id = page.dotimgId), // Ссылка на векторное изображение
                contentDescription = "..",
                contentScale = ContentScale.Fit, // Масштабирование изображения
                modifier = Modifier.size(50.dp) // Размер изображения
                //  .padding(top = 20.dp)
            )
        }
        Image(
            painter = painterResource(id = page.imgId), // Ссылка на векторное изображение
            contentDescription = "..",
            contentScale = ContentScale.Fit, // Масштабирование изображения
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(400.dp) // Размер изображения
            //  .padding(top = 20.dp)
        )
    }
}

data class PageData(
    val title: String,
    val description: String,
    val imgId:Int,
    val dotimgId:Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(navController: NavController) {
    Box(Modifier.fillMaxSize()) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp), Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.mipmap.hello_foreground), // Ссылка на векторное изображение
                    contentDescription = "..",
                    contentScale = ContentScale.Fit, // Масштабирование изображения
                    modifier = Modifier.size(30.dp) // Размер изображения
                    // .padding(top = 10.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Добро пожаловать!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
            }
            Row() {
                Text(
                    text = "Войдите, чтобы пользоваться функциями приложения",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(15.dp)
                )
            }

            Row {
                Column {
                    var email by remember { mutableStateOf("") }
                    var isValidEmail by remember { mutableStateOf(false) }
                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
                    Text(
                        text = "Вход по E-mail",
                        modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { newValue ->
                            email = newValue
                            isValidEmail = emailRegex.matches(newValue) // Проверка email
                            },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        enabled = true,
                        readOnly = false,
                        placeholder = {
                            Text(
                                text = "example@mail.ru",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 20.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black.copy(0.5f),
                            unfocusedTextColor = Color.Black.copy(0.5f),
                            focusedBorderColor = Color.Black.copy(0.5f),
                            unfocusedBorderColor = Color.Black.copy(0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    PrimaryButton(
                        text = "Далее",
                        enabled = isValidEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp),
                        onClick = {navController.navigate("prooveEmail")})
                }
            }

        }
    }
}

@Composable
fun ProoveEmail(navController: NavController,
                onResendCode: () -> Unit, // Callback для повторной отправки кода

                 ) {
        // Состояние для ввода кода
        var code by remember { mutableStateOf("") }
        val maxCodeLength = 6

        // Состояние для таймера
        var timerSeconds by remember { mutableStateOf(60) }
        var isTimerRunning by remember { mutableStateOf(true) }

    val textFieldValues = remember { mutableStateListOf("", "", "", "", "", "") }

    val allFieldsFilled = textFieldValues.all { it.isNotBlank() }
        // Эффект для таймера
        LaunchedEffect(key1 = isTimerRunning) {
            if (isTimerRunning) {
                while (timerSeconds > 0) {
                    delay(1000L) // Задержка на 1 секунду
                    timerSeconds--
                }
                isTimerRunning = false
            }
        }

        // Основная разметка экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Текст заголовка
            Text(
                text = "Введите код из E-mail",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Поле ввода для кода с квадратами
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                textFieldValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            textFieldValues[index] = newValue
                                if(allFieldsFilled ){
                                    //TODO переход на след экран
                                    navController.navigate("splash")
                                }

                        },
                        modifier = Modifier
                            .width(42.dp)
                            .height(48.dp)
                            .padding(top = 1.dp), // Небольшой отступ
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Текст с таймером или кнопкой отправки кода
            if (isTimerRunning) {
                Text(
                    text = "Отправить код повторно можно\nбудет через $timerSeconds секунд",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                    textAlign = TextAlign.Center
                )
            } else {
                PrimaryButton(modifier = Modifier, text = "Выслать код повторно",onClick = {
                    // Сбросить таймер и вызвать повторную отправку кода
                    timerSeconds = 60
                    isTimerRunning = true
                    onResendCode()
                })
            }
        }
    }

fun ResendCode(){}

import kotlinx.coroutines.runBlocking
import kotlin.random.Random

// Генерация 6-значного кода
fun generateVerificationCode(): String {
    return (100000..999999).random().toString()
}

// Отправка кода на email
fun sendVerificationCode(email: String) {
    val verificationCode = generateVerificationCode()

    runBlocking {
        try {
            //supabase.pluginManager.getPlugin(GoTrue).sendEmail(
               // email = email,
                //subject = "Код подтверждения",
                //body = "Ваш код подтверждения: $verificationCode"
            //)
            println("Код подтверждения отправлен на $email")
        } catch (e: Exception) {
            println("Ошибка отправки кода: ${e.message}")
        }
    }
}




