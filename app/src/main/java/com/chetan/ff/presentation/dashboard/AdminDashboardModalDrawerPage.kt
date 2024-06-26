package com.chetan.ff.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SocialDistance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chetan.ff.presentation.animation.HoverAnimation

@Composable
fun AdminDashboardModalDrawerPage(
    onClick: (MenuItem) -> Unit,
    state: DashboardState,
    groupSelected: (String) -> Unit,
) {
    val bottomMenuItem = listOf(
        MenuItem.Logout
    )

    val requestMenuItem = listOf(
        MenuItem.RequestStatus,
        MenuItem.JoinGroup,
        MenuItem.OurLocations
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.primary
                    )
                ),
                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                alpha = 0.5f
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.padding(5.dp),
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Group Activities",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = {
                    onClick(MenuItem.CreateGroup)
                }) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Create Group",
                    )
                }
                IconButton(onClick = { onClick(MenuItem.JoinGroup) }) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "Join Group",
                    )
                }
                IconButton(onClick = { onClick(MenuItem.GroupRequest) }) {
                    Icon(
                        imageVector = if (state.groupRequestList.isEmpty()) Icons.Default.NotificationsActive else Icons.Default.NotificationAdd,
                        contentDescription = "Request",
                    )
                }
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            HoverAnimation(requestMenuItem) {
                onClick(it)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (state.groupList.isEmpty()) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(state.groupList) {
                    Card(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .clickable {
                                groupSelected(it.groupName)
                            },
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.8f
                            )
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            text = it.groupName, style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(text = it.groupCreated)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
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
    data object RequestStatus :
        MenuItem(icon = Icons.Default.RemoveRedEye, label = "Request Status")
    data object OurLocations: MenuItem(icon = Icons.Default.SocialDistance, label = "Our Locations")

    data object Logout : MenuItem(icon = Icons.Default.Logout, label = "LogOut")
    data object CreateGroup : MenuItem(icon = Icons.Default.Create, label = "Create Group")
    data object JoinGroup : MenuItem(icon = Icons.Default.Create, label = "Join Group")
    data object GroupRequest : MenuItem(icon = Icons.Default.Create, label = "Group Request")
}