package com.chetan.ff.presentation.dashboard

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.chetan.ff.R
import com.chetan.ff.common.ApplicationAction
import com.chetan.ff.presentation.dashboard.home.HomeScreen
import com.chetan.ff.presentation.dashboard.home.HomeViewModel
import com.chetan.ff.presentation.dashboard.library.LibraryScreen
import com.chetan.ff.presentation.dialogs.MessageDialog
import com.chetan.ff.utils.BottomNavigate.bottomNavigate
import com.chetan.ff.utils.LoadLottieAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class InnerPage(
    val route: String,
    val label: Int,
    val icon: ImageVector,
    val count: String = "",
    val isBadge: Boolean = false
)

@Composable
fun DashboardScreen(
    nav: NavHostController,
    onBack: () -> Unit,
    state: DashboardState,
    onEvent: (event: DashboardEvent) -> Unit,
    onAction: (ApplicationAction) -> Unit
) {
    val bottomNavController = rememberNavController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var backPressCount by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = backPressCount, block = {
        if (backPressCount == 1) {
            delay(2000)
            backPressCount = 0
        }
    })
    BackHandler {
        if (backPressCount == 0) {
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
            backPressCount++
        } else if (backPressCount == 1) {
            onBack()
        }
    }
    val items: List<InnerPage> = remember {
        listOf(
            InnerPage("home", R.string.home, Icons.Default.Home),
            InnerPage("library", R.string.library, Icons.Default.LibraryMusic)
        )
    }
    var profileImgUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    var showDailog by remember {
        mutableStateOf(false)
    }
    if (showDailog && profileImgUri != Uri.EMPTY) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(elevation = CardDefaults.cardElevation(10.dp)) {
                AsyncImage(modifier = Modifier, model = profileImgUri, contentDescription = "")
                Row {
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(0.5f),
                        elevation = ButtonDefaults.buttonElevation(10.dp),
                        onClick = {
                            onEvent(DashboardEvent.UploadImage(profileImgUri))
                            showDailog = false
                        }) {
                        Text(text = "Upload")
                    }
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(0.5f),
                        elevation = ButtonDefaults.buttonElevation(10.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        onClick = {
                            showDailog = false
                            profileImgUri = Uri.EMPTY
                        }) {
                        Text(text = "Cancel")
                    }
                }

            }
        }
    }
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    profileImgUri = uri
                    showDailog = true
                }

            })
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var hideOpenBar by remember {
        mutableStateOf(true)
    }
    SideEffect {
        scope.launch {
            delay(5000)
            hideOpenBar = false
        }

    }

    var showCreateGroupDialog by remember {
        mutableStateOf(false)
    }

    var showJoinGroupDialog by remember {
        mutableStateOf(false)
    }
    var showRequestGroupDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.groupName, block = {
        if (state.groupName.isNotBlank()) {
            onAction(ApplicationAction.Restart)
        }
    })

    ModalNavigationDrawer(
        gesturesEnabled = true,
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        drawerContent = {
            AdminDashboardModalDrawerPage(
                state = state,
                onClick = {
                    when (it) {
                        MenuItem.SendNotice -> {

                        }

                        MenuItem.Logout -> {
                            onAction(ApplicationAction.Logout)
                        }

                        MenuItem.CreateGroup -> {
                            showCreateGroupDialog = true
                        }

                        MenuItem.GroupRequest -> {
                            showRequestGroupDialog = true
                        }

                        MenuItem.JoinGroup -> {
                            showJoinGroupDialog = true
                        }

                        MenuItem.RequestStatus -> {

                        }
                    }
                },
                groupSelected = {
                    onEvent(DashboardEvent.ChangeMyGroup(it))
                }
            )
        })
    {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent
                ) {
                    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                    items.forEach { screen ->
                        val isSelected =
                            navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true
                        val color =
                            if (isSelected) Color.White else MaterialTheme.colorScheme.outline

                        CompositionLocalProvider(LocalContentColor provides color) {
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                        LocalAbsoluteTonalElevation.current
                                    )
                                ),
                                icon = {
                                    Card(
                                        modifier = Modifier.size(34.dp),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                        elevation = CardDefaults.cardElevation(10.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(2.dp)
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .size(20.dp),
                                                imageVector = screen.icon,
                                                tint = color,
                                                contentDescription = ""
                                            )
                                            Text(
                                                text = if (screen.isBadge) "" else "",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(end = 2.dp),
                                                fontSize = 8.sp,
                                                textAlign = TextAlign.Right,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                    }
                                },
                                selected = isSelected,
                                onClick = { bottomNavController.bottomNavigate(screen.route) },
                                label = {},
                                alwaysShowLabel = false
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(40.dp),
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "")
                }
            },
        ) {

            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (onEvent != null && state.infoMsg.isCancellable == true) {
                        onEvent(DashboardEvent.DismissInfoMsg)
                    }
                }, onPositive = { /*TODO*/ }) {

                }
            }
            Box {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = bottomNavController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        val viewModel = hiltViewModel<HomeViewModel>()
                        HomeScreen(
                            navController = nav,
                            event = viewModel.onEvent,
                            state = viewModel.state.collectAsStateWithLifecycle().value
                        )
                    }
                    composable("library") {
                        LibraryScreen(
                            navController = nav
                        )
                    }
                }
                if (hideOpenBar) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.KeyboardDoubleArrowRight,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }

        }
    }

    if (showCreateGroupDialog) {
        Dialog(onDismissRequest = {

        }
        ) {
            val ctx = LocalContext.current
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Create Your Group",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                LoadLottieAnimation(
                    modifier = Modifier.size(200.dp),
                    image = R.raw.groups
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.onChangeGroupName,
                    onValueChange = {
                        onEvent(DashboardEvent.OnGroupNameChange(it))
                    },
                    label = {
                        Text("Group Name")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = state.onChangeGroupName.isNotBlank(),
                        onClick = {
                            onEvent(DashboardEvent.SetGroupName(state.onChangeGroupName))
                        }) {
                        Text(text = "Create")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        onClick = {
                            showCreateGroupDialog = false
                        }) {
                        Text(text = "Cancel")
                    }
                }

            }
        }
    }

    if (showJoinGroupDialog) {
        Dialog(onDismissRequest = {

        }
        ) {
            val ctx = LocalContext.current
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Join Group",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.onChangeAdminGmail,
                    onValueChange = {
                        onEvent(DashboardEvent.OnChangeAdminGmail(it))
                    },
                    label = {
                        if (state.onChangeAdminGmail.isBlank()) {
                            Text("Admin Gmail")
                        } else {
                            if (state.onChangeAdminGmail.contains("@")) {
                                Text("Admin Gmail")
                            } else {
                                Text(
                                    text = "Invalid Gmail",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }


                    }
                )
                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = state.onChangeGroupName,
                    onValueChange = {
                        onEvent(DashboardEvent.OnGroupNameChange(it))
                    },
                    label = {
                        Text("Group Name")
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = state.onChangeGroupName.isNotBlank() && state.onChangeAdminGmail.contains(
                            "@"
                        ),
                        onClick = {
                            onEvent(
                                DashboardEvent.SendGroupRequest(
                                    state.onChangeAdminGmail,
                                    state.onChangeGroupName
                                )
                            )
                            showJoinGroupDialog = false
                        }) {
                        Text(text = "Request")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        onClick = {
                            showJoinGroupDialog = false
                        }) {
                        Text(text = "Cancel")
                    }
                }

            }
        }
    }

    if (showRequestGroupDialog) {
        Dialog(onDismissRequest = {
            showRequestGroupDialog = false
        }
        ) {
            val ctx = LocalContext.current
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Requests",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                state.groupRequestList.forEach {data ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = data.groupName, fontWeight = FontWeight.Bold)
                                Text(text = data.groupRequested)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                IconButton(onClick = {
                                    onEvent(DashboardEvent.AcceptGroupRequest(data))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "done"
                                    )
                                }

                                IconButton(onClick = {

                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete"
                                    )
                                }
                            }

                        }
                    }
                }




                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showRequestGroupDialog = false
                        }) {
                        Text(text = "Done")
                    }
                }

            }
        }
    }


}