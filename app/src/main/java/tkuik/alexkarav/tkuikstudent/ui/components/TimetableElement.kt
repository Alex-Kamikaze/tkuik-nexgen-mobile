package tkuik.alexkarav.tkuikstudent.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tkuik.alexkarav.tkuikstudent.domain.models.TimetableModel

@Composable
fun TimetableElement(pairNum: Int, pairName: String, cabinet: String, teacherName: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end = 24.dp, top = 15.dp)
        .height(IntrinsicSize.Max), shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(12.dp))
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp), verticalArrangement = Arrangement.SpaceAround) {
            Text("$pairNum пара", fontWeight = FontWeight.Black, fontSize = 24.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text(pairName, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text("Преподаватель: $teacherName", fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text("Кабинет: $cabinet", fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        }
    }
}