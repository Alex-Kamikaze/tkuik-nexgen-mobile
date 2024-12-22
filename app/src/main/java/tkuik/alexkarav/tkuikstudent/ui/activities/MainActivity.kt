package tkuik.alexkarav.tkuikstudent.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tkuik.alexkarav.tkuikstudent.R
import tkuik.alexkarav.tkuikstudent.domain.AuthViewModel
import tkuik.alexkarav.tkuikstudent.domain.MainViewModel
import tkuik.alexkarav.tkuikstudent.domain.TimetableViewModel
import tkuik.alexkarav.tkuikstudent.domain.models.BottomNavigationElement
import tkuik.alexkarav.tkuikstudent.ui.screens.AuthorizationScreen
import tkuik.alexkarav.tkuikstudent.ui.screens.DocumentOrderingScreen
import tkuik.alexkarav.tkuikstudent.ui.screens.TimetableScreen
import tkuik.alexkarav.tkuikstudent.ui.theme.TkuikStudentTheme
import tkuik.alexkarav.tkuikstudent.utils.UIState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val timetableViewModel by viewModels<TimetableViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.splashReady.value
            }
        }
        val bottomNavigationItems = listOf(
            BottomNavigationElement(
                "Расписание",
                Icons.Outlined.DateRange,
                "timetable",
                Icons.Filled.DateRange
            ),
            BottomNavigationElement(
                "Справки",
                Icons.Outlined.Info,
                "documents",
                Icons.Filled.Info
            ),
            BottomNavigationElement(
                "Профиль",
                Icons.Outlined.AccountCircle,
                "profile",
                Icons.Filled.AccountCircle
            )
        )

        super.onCreate(savedInstanceState)
        setContent {
            TkuikStudentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navController = rememberNavController()
                    val selectedRoute = rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    val bottomBarVisible by mainViewModel.showBottomBar.collectAsState(initial = false)
                    val skipAuth by mainViewModel.skipAuthorization.collectAsState()

                    val onAuthButtonPress: (String, String, Int) -> Unit =
                        { login, password, group ->
                            authViewModel.authorizeUser(login, password, group)
                        }
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(visible = bottomBarVisible, enter = slideInVertically(), exit = slideOutVertically()) {
                                NavigationBar {
                                    bottomNavigationItems.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = index == selectedRoute.intValue,
                                            onClick = {
                                                navController.navigate(item.itemRoute);
                                                selectedRoute.intValue = index
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = if (index == selectedRoute.intValue) item.itemIconChosen else item.itemIconOutlined,
                                                    contentDescription = null
                                                )
                                            },
                                            alwaysShowLabel = true,
                                            label = { Text(item.itemText) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = colorResource(id = R.color.splashscreen_icon_background),
                                                indicatorColor = colorResource(id = R.color.splashscreen_icon_background).copy(
                                                    alpha = 0.2f
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "pending"
                        ) {

                            composable("pending") {
                                LaunchedEffect(skipAuth) {
                                    if(skipAuth) {
                                        mainViewModel.setBottomBarVisibility(true)
                                        navController.navigate("timetable") {
                                            popUpTo("pending") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }

                                CircularProgressIndicator(modifier = Modifier.size(150.dp))
                            }

                            composable("auth") {
                                val authorized by authViewModel.authorized.collectAsState()
                                val groupList by authViewModel.groupList.collectAsState()

                                val onRetry = {
                                    authViewModel.loadGroupList()
                                }
                                LaunchedEffect(Unit) {
                                    authViewModel.loadGroupList()
                                }

                                LaunchedEffect(authorized) {
                                    if (authorized is UIState.Success<*>) {
                                        mainViewModel.setBottomBarVisibility(true)
                                        navController.navigate("timetable") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                }

                                AuthorizationScreen(
                                    Modifier.padding(paddingValues),
                                    groupList,
                                    onAuthButtonPress,
                                    onRetry
                                )
                            }
                            composable("timetable") {
                                LaunchedEffect(Unit) {
                                    mainViewModel.setBottomBarVisibility(true)
                                }
                                TimetableScreen(Modifier.padding(paddingValues), timetableViewModel)
                            }

                            composable("documents") {
                                DocumentOrderingScreen()
                            }

                            composable("profile") {
                                DocumentOrderingScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
