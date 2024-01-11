package com.chetan.ff.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardModalDrawerPage(
    onClick: (MenuItem) -> Unit,
    state: DashboardState,
) {
    val menuList = listOf(
        MenuItem.SendNotice,
    )
    val bottomMenuItem = listOf(
        MenuItem.Logout
    )

    val requestMenuItem = listOf(
        MenuItem.GroupRequest,
        MenuItem.RequestStatus
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Text(text = "Groups")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = {
                    onClick(MenuItem.CreateGroup)
                }) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "Create Group",
                    )
                }
                IconButton(onClick = { onClick(MenuItem.JoinGroup) }) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Join Group",
                    )
                }
                IconButton(onClick = { onClick(MenuItem.GroupRequest) }) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Request",
                    )
                }
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .width(50.dp)
            ) {
            requestMenuItem.forEach {
                IconButton(onClick = { onClick(MenuItem.JoinGroup) }) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.label,
                    )
                }
            }

            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        5.dp
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                items(10) {
                    Card(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.8f
                            )
                        )
                    ) {
                        Text(text = "Group ${it}")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
        Divider()
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            bottomMenuItem.forEach { item ->
                ElevatedCard(
                    modifier = Modifier
                        .clickable {
                            onClick(item)
                        },
                    shape = RoundedCornerShape(5.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
            Spacer(modifier = Modifier.height(50.dp))
        }

    }

}


sealed class MenuItem(val icon: ImageVector, val label: String) {
    data object SendNotice : MenuItem(icon = Icons.Default.NotificationAdd, label = "Send Notice")
    data object RequestStatus : MenuItem(icon = Icons.Default.QueryStats, label = "Request Status")
    data object Logout : MenuItem(icon = Icons.Default.Logout, label = "LogOut")
    data object CreateGroup : MenuItem(icon = Icons.Default.Create, label = "Create Group")
    data object JoinGroup : MenuItem(icon = Icons.Default.Create, label = "Join Group")
    data object GroupRequest : MenuItem(icon = Icons.Default.Create, label = "Group Request")
}