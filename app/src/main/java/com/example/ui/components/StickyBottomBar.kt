package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun StickyBottomActionBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyPrimary)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Sticky Mobile CTA Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCallClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GoldAccent)
                ),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    modifier = Modifier.size(16.dp),
                    tint = GoldAccent
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "CALL",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                )
            }

            OutlinedButton(
                onClick = onWhatsAppClick,
                modifier = Modifier
                    .weight(1.2f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(StatusCompleted)
                ),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(16.dp),
                    tint = StatusCompleted
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "WHATSAPP",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                )
            }

            Button(
                onClick = onBookClick,
                modifier = Modifier
                    .weight(1.8f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = NavyPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Book",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "BOOK APPT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                )
            }
        }

        // Navigation Bar Tabs
        NavigationBar(
            containerColor = NavyLight,
            contentColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.height(60.dp)
        ) {
            NavigationBarItem(
                selected = currentTab == 0,
                onClick = { onTabSelected(0) },
                icon = { Icon(imageVector = if (currentTab == 0) Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                )
            )

            NavigationBarItem(
                selected = currentTab == 1,
                onClick = { onTabSelected(1) },
                icon = { Icon(imageVector = if (currentTab == 1) Icons.Default.MedicalServices else Icons.Outlined.MedicalServices, contentDescription = "Services") },
                label = { Text("Tariff", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                )
            )

            NavigationBarItem(
                selected = currentTab == 2,
                onClick = { onTabSelected(2) },
                icon = { Icon(imageVector = if (currentTab == 2) Icons.Default.CalendarMonth else Icons.Outlined.CalendarMonth, contentDescription = "Book") },
                label = { Text("Booking", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                )
            )

            NavigationBarItem(
                selected = currentTab == 3,
                onClick = { onTabSelected(3) },
                icon = { Icon(imageVector = if (currentTab == 3) Icons.Default.Article else Icons.Outlined.Article, contentDescription = "Knowledge") },
                label = { Text("Spine Hub", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                )
            )

            NavigationBarItem(
                selected = currentTab == 4,
                onClick = { onTabSelected(4) },
                icon = { Icon(imageVector = if (currentTab == 4) Icons.Default.LocationOn else Icons.Outlined.LocationOn, contentDescription = "Contact") },
                label = { Text("Contact", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyPrimary,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                )
            )
        }
    }
}
