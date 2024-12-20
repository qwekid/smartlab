package com.example.smartlab

import PrimaryButton
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.smartlab.ui.theme.HeaderColor
import com.example.smartlab.ui.theme.SmartLabTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartLabTheme {
                AppNavigator(this)
            }
        }
    }
}

@Composable
fun AppNavigator(context: Context) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("main") { LogInScreen(navController) }
        composable("prooveEmail") { ProoveEmail(onResendCode = {SendCode()}, navController = navController) }
        composable("setPasswordScreen") { SetPasswordScreen(navController,context) }
        composable("CreateCardScreen") { CreateCardScreen(navController) }
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
        composable("prooveEmail") { ProoveEmail(onResendCode = {SendCode()}, navController = navController) }
    }
    SmartLabTheme {
        CreateCardScreen(navController)
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
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
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
fun ProoveEmail(navController: NavController, onResendCode: () -> Unit, ) {
        // Состояние для ввода кода
        var code by remember { mutableStateOf(generateVerificationCode()) }
        val maxCodeLength = 6

        // Состояние для таймера
        var timerSeconds by remember { mutableStateOf(60) }
        var isTimerRunning by remember { mutableStateOf(true) }

        val textFieldValues = remember { mutableStateListOf("", "", "", "", "", "") }
        val focusRequesters = remember { List(6) { FocusRequester() } }

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
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 15.dp)) {
            Button(onClick = {navController.navigate("main")}, modifier = Modifier.padding(0.dp), contentPadding = PaddingValues(0.dp), shape = RoundedCornerShape(0.dp), colors = ButtonDefaults.textButtonColors(containerColor = Color.White, disabledContainerColor = Color.White)) {
                Image(
                    painter = painterResource(id = R.drawable.back), // Ссылка на векторное изображение
                    contentDescription = "..",
                    contentScale = ContentScale.Fit, // Масштабирование изображения
                    modifier = Modifier
                        .size(30.dp) // Размер изображения

                        .fillMaxSize()

                ) }
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
                )
                {
                    textFieldValues.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    textFieldValues[index] = newValue

                                    if (newValue.length == 1 && index == textFieldValues.lastIndex) {
                                        if(textFieldValues.joinToString("")==code)
                                        // переход на следующий экран
                                        navController.navigate("setPasswordScreen")
                                    }
                                    if (newValue.length == 1 && index < textFieldValues.lastIndex) {
                                        // Переключаем фокус на следующее поле
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(42.dp)
                                .height(48.dp)
                                .padding(top = 1.dp)
                                .focusRequester(focusRequesters[index]), // Устанавливаем FocusRequester
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
                    PrimaryButton(modifier = Modifier, text = "Выслать код повторно", onClick = {
                        // Сбросить таймер и вызвать повторную отправку кода
                        timerSeconds = 60
                        isTimerRunning = true
                        onResendCode()
                    })
                }
            }
        }
    }

fun SendCode(){

}
// Генерация 6-значного кода
fun generateVerificationCode(): String {
    return 111111.toString()
    //пока что генерпция кода не реализована, т.к. не работает отправка через email
    //return (100000..999999).random().toString()
}

//TODO Отправка кода на email
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

@Composable
fun SetPasswordScreen(navController: NavController, context:Context) {

    var passFieldValues = remember { mutableStateListOf("", "", "", "") }
    var indexOfPassChar by remember { mutableIntStateOf(0) }

    val sharedPreferences = remember { getEncryptedSharedPreferences(context) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { navController.navigate("CreateCardScreen") },
                    modifier = Modifier
                        //.align(Alignment.Start)
                        .padding(16.dp)
                ) {
                    Text("Пропустить")
                }
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(
                            text = "Создайте пароль",
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            lineHeight = 29.sp
                        )
                    }
                    Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = "Для защиты ваших персональных данных",
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            Row(modifier = Modifier. padding(top = 30.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                passFieldValues.forEachIndexed { index, value ->
                    var painterr = painterResource(id = R.drawable.ellipse_notfilled)
                    if (passFieldValues[index]!=""){
                        painterr = painterResource(id = R.drawable.ellipse_filled)
                    }
                    Image(
                        painter = painterr, // Ссылка на векторное изображение
                        contentDescription = "..",
                        contentScale = ContentScale.Fit, // Масштабирование изображения
                        modifier = Modifier
                            .size(20.dp) // Размер изображения
                            .fillMaxSize()
                    )
                }
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (row in 0 until 3) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterHorizontally)) {
                            for (col in 0 until 3) {
                                val buttonNumber = row * 3 + col + 1
                                Button(
                                    onClick = {
                                        if(indexOfPassChar<=2){
                                            passFieldValues[indexOfPassChar]=buttonNumber.toString()
                                            indexOfPassChar++
                                        }
                                        else{
                                            if(indexOfPassChar==3){
                                                passFieldValues[indexOfPassChar]=buttonNumber.toString()
                                                //сохранение пароля
                                                val concatenatedValues = passFieldValues.joinToString("")
                                                sharedPreferences.edit().putString("passFieldValues", concatenatedValues).apply()
                                                //переход на некст экран
                                                navController.navigate("CreateCardScreen")
                                            }
                                        }
                                              },
                                    shape = RoundedCornerShape(100.dp),
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF5F5F9),
                                        disabledContainerColor = Color(0xFFF5F5F9)
                                    )
                                ) {
                                    Text(text = buttonNumber.toString(), color = Color.Black, fontSize = 24.sp)
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterHorizontally)) {
                        Column(){
                            Box(modifier = Modifier
                                .height(100.dp)
                                .width(100.dp))
                        }
                        Column {
                            Button(
                                onClick = {
                                    if(indexOfPassChar<=2){
                                        passFieldValues[indexOfPassChar]=0.toString()
                                        indexOfPassChar++
                                    }
                                    else{
                                        if(indexOfPassChar==3){
                                            passFieldValues[indexOfPassChar]=0.toString()
                                            //сохранение пароля
                                            val concatenatedValues = passFieldValues.joinToString("")
                                            sharedPreferences.edit().putString("passFieldValues", concatenatedValues).apply()
                                            //переход на некст экран
                                            navController.navigate("CreateCardScreen")
                                        }
                                    }
                                          },
                                shape = RoundedCornerShape(100.dp),
                                modifier = Modifier
                                    .height(90.dp)
                                    .width(90.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF5F5F9),
                                    disabledContainerColor = Color(0xFFF5F5F9)
                                )
                            ) {
                                Text(text = 0.toString(), color = Color.Black, fontSize = 24.sp)
                            }
                        }
                        Column {
                            Box(modifier = Modifier
                                .height(100.dp)
                                .width(100.dp), contentAlignment = Alignment.Center) {
                                Button(
                                    onClick = {
                                        if(indexOfPassChar<5){
                                            if(indexOfPassChar>=1){
                                                passFieldValues[indexOfPassChar-1]=""
                                                indexOfPassChar--
                                            }
                                            if(indexOfPassChar==0){
                                                passFieldValues[indexOfPassChar]=""
                                            }
                                        }
                                    else {

                                    }
                                              }, colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = Color.White)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.del_icon), // Ссылка на векторное изображение
                                        contentDescription = "..",
                                        contentScale = ContentScale.Fit, // Масштабирование изображения
                                        modifier = Modifier
                                            .size(35.dp) // Размер изображения
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))

                }
            }

        }
    }
}


fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        "encrypted_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardScreen(navController: NavController) {
    Box(Modifier.fillMaxSize().background(Color.White)){
        Column(Modifier.padding(vertical = 20.dp, horizontal = 15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Создание карты\n пациента",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier
                        //.align(Alignment.Start)
                        .padding(16.dp)
                ) {
                    Text("Пропустить")
                }
            }
            Row {
                Text(text = "Без карты пациента вы не сможете заказать\nанализы.", color = Color.Black.copy(0.5f), fontSize = 14.sp)
            }
            Row{
                Text(text = "В картах пациентов будут храниться результаты\nанализов вас и ваших близких.", color = Color.Black.copy(0.5f), fontSize = 14.sp)
            }
            Box{
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    Row{
                        var textState by remember { mutableStateOf(TextFieldValue("")) }
                        OutlinedTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            //  .padding(start = 10.dp, end = 10.dp),
                            enabled = true,
                            readOnly = false,
                            placeholder = {
                                Text(text = "Имя",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp)
                            },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEBEBEB),
                                unfocusedContainerColor = Color(0xFFEBEBEB),
                                focusedTextColor = Color.Black.copy(0.5f),
                                unfocusedTextColor = Color.Black.copy(0.5f),
                                focusedBorderColor = Color(0xFFEBEBEB),
                                unfocusedBorderColor = Color(0xFFEBEBEB)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                    Row{
                        var textState by remember { mutableStateOf(TextFieldValue("")) }
                        OutlinedTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            //  .padding(start = 10.dp, end = 10.dp),
                            enabled = true,
                            readOnly = false,
                            placeholder = {
                                Text(text = "Отчество",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp)
                            },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEBEBEB),
                                unfocusedContainerColor = Color(0xFFEBEBEB),
                                focusedTextColor = Color.Black.copy(0.5f),
                                unfocusedTextColor = Color.Black.copy(0.5f),
                                focusedBorderColor = Color(0xFFEBEBEB),
                                unfocusedBorderColor = Color(0xFFEBEBEB)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                    Row{
                        var textState by remember { mutableStateOf(TextFieldValue("")) }
                        OutlinedTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            //  .padding(start = 10.dp, end = 10.dp),
                            enabled = true,
                            readOnly = false,
                            placeholder = {
                                Text(text = "Фамилия",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp)
                            },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEBEBEB),
                                unfocusedContainerColor = Color(0xFFEBEBEB),
                                focusedTextColor = Color.Black.copy(0.5f),
                                unfocusedTextColor = Color.Black.copy(0.5f),
                                focusedBorderColor = Color(0xFFEBEBEB),
                                unfocusedBorderColor = Color(0xFFEBEBEB)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                    Row{
                        var textState by remember { mutableStateOf(TextFieldValue("")) }
                        OutlinedTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            //  .padding(start = 10.dp, end = 10.dp),
                            enabled = true,
                            readOnly = false,
                            placeholder = {
                                Text(text = "Дата рождения",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp)
                            },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEBEBEB),
                                unfocusedContainerColor = Color(0xFFEBEBEB),
                                focusedTextColor = Color.Black.copy(0.5f),
                                unfocusedTextColor = Color.Black.copy(0.5f),
                                focusedBorderColor = Color(0xFFEBEBEB),
                                unfocusedBorderColor = Color(0xFFEBEBEB)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                    Row{
                        var expanded by remember { mutableStateOf(false) }
                        var selectedGender by remember { mutableStateOf("Пол") }
                        val genders = listOf("Мужской", "Женский")

                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedGender,
                                onValueChange = { /* Ничего не делаем, так как поле только для выбора */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = true },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icons),
                                        contentDescription = "Custom Icon"
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFEBEBEB),
                                    unfocusedContainerColor = Color(0xFFEBEBEB),
                                    focusedTextColor = Color.Black.copy(0.5f),
                                    unfocusedTextColor = Color.Black.copy(0.5f),
                                    focusedBorderColor = Color(0xFFEBEBEB),
                                    unfocusedBorderColor = Color(0xFFEBEBEB)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                genders.forEach { gender ->
                                    DropdownMenuItem(onClick = {
                                        selectedGender = gender
                                        expanded = false
                                    },
                                        text = {Text(text = gender)})
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        PrimaryButton(text = "Создать", onClick = {insertDataOnDB()}, modifier = Modifier.fillMaxWidth().height(50.dp).padding(0.dp))
                    }
                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }
}
fun insertDataOnDB(){}



