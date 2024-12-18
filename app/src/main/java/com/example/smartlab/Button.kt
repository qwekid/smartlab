import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartlab.ui.theme.ButtonColor
import com.example.smartlab.ui.theme.DisabledButtonColor

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier,
    enabled:Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .padding(8.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = ButtonColor,
            disabledContainerColor = DisabledButtonColor
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 17.sp)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    PrimaryButton(text = "Нажми меня", modifier = Modifier) {
    }
}