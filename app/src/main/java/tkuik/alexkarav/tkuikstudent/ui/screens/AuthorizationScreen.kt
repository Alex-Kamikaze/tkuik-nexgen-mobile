package tkuik.alexkarav.tkuikstudent.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tkuik.alexkarav.tkuikstudent.R
import tkuik.alexkarav.tkuikstudent.data.remote.models.GroupRemoteModel
import tkuik.alexkarav.tkuikstudent.utils.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationScreen(modifier: Modifier = Modifier, groupList: UIState, onAuthButtonPress: (String, String, Int) -> Unit, onRetry: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var expanded by remember { mutableStateOf(false) }
    var chosenGroup by remember { mutableIntStateOf(0) }

    when(groupList) {
        is UIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(150.dp))
            }
        }

        is UIState.Error -> {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Outlined.Warning, contentDescription = null, modifier = Modifier.size(120.dp), tint = Color.Red)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Произошла ошибка при загрузке данных. Проверьте подключение к интернету и попробуйте позже", fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(onClick = {
                    onRetry()
                }, modifier = Modifier.width(200.dp), shape = RoundedCornerShape(5.dp)) {
                    Text("Повторить")
                }
            }
        }

        is UIState.Success<*> -> {
            val groupList = groupList.data as List<GroupRemoteModel>
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.mipmap.splashscreen_icon_foreground), contentDescription = null, modifier = Modifier.padding(bottom = 10.dp))
                    OutlinedTextField(value = login, onValueChange = { login = it }, placeholder = { Text("Логин") })
                    OutlinedTextField(value = password, onValueChange = { password = it }, placeholder = { Text("Пароль") }, modifier = Modifier.padding(top = 10.dp))
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it}, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 55.dp, end = 55.dp, top = 10.dp)) {
                        OutlinedTextField(
                            value = groupList[chosenGroup].groupName,
                            onValueChange = {},
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            placeholder = { Text("Выбери свою группу")}
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
                            groupList.forEachIndexed { index, group ->
                                DropdownMenuItem(text = { Text(group.groupName) }, onClick = { chosenGroup = index; expanded = false })
                            }
                        }

                    }
                    Button(onClick = { onAuthButtonPress(login, password, groupList[chosenGroup].id) }, modifier = Modifier
                        .padding(top = 10.dp, start = 55.dp, end = 55.dp)
                        .fillMaxWidth(), shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = colorResource(R.color.splashScreenBackground), contentColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(12.dp)) {
                        Text("Войти")
                    }
                }
            }
        }

        is UIState.Pending -> {

        }
    }



}