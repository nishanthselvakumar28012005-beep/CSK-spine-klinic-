package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AdminState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    adminState: AdminState,
    appointments: List<AppointmentEntity>,
    services: List<ServiceEntity>,
    clinicHours: List<ClinicHoursEntity>,
    blockedSlots: List<BlockedSlotEntity>,
    onLoginPin: (String) -> Unit,
    onLogout: () -> Unit,
    onUpdateStatus: (String, String) -> Unit,
    onBlockSlot: (String, String, String) -> Unit,
    onUnblockSlot: (BlockedSlotEntity) -> Unit,
    onToggleClinicDay: (ClinicHoursEntity) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFilterStatusChange: (String) -> Unit
) {
    if (!adminState.isAuthenticated) {
        // --- ADMIN LOGIN SCREEN ---
        var pinText by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NavyPrimary)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = NavyPrimary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Text(
                        text = "Admin Portal Authentication",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )

                    Text(
                        text = "Enter Clinic Admin Security PIN (Default: 1234)",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                    )

                    OutlinedTextField(
                        value = pinText,
                        onValueChange = { pinText = it },
                        label = { Text("4-Digit PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    adminState.errorMessage?.let { error ->
                        Text(text = error, color = RedAccent, style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = { onLoginPin(pinText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            contentColor = NavyPrimary
                        )
                    ) {
                        Text("LOGIN TO ADMIN DASHBOARD", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // --- ADMIN DASHBOARD ---
        var activeSubTab by remember { mutableStateOf(0) } // 0=Appointments, 1=Block Time, 2=Clinic Hours

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Admin Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Clinic Admin Dashboard",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )
                    Text("Charlie's Spine Klinic Management", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)))
                }

                IconButton(onClick = onLogout) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = RedAccent)
                }
            }

            // Metric Overview Cards
            val pendingCount = appointments.count { it.status == "pending" }
            val confirmedCount = appointments.count { it.status == "confirmed" }
            val completedCount = appointments.count { it.status == "completed" }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip(modifier = Modifier.weight(1f), title = "Pending", count = pendingCount, color = StatusPending)
                MetricChip(modifier = Modifier.weight(1f), title = "Confirmed", count = confirmedCount, color = StatusConfirmed)
                MetricChip(modifier = Modifier.weight(1f), title = "Completed", count = completedCount, color = StatusCompleted)
            }

            // Sub Navigation Tabs
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = SlateSurface,
                contentColor = NavyPrimary
            ) {
                Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }) {
                    Text("Appointments", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }) {
                    Text("Block Slots", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Tab(selected = activeSubTab == 2, onClick = { activeSubTab = 2 }) {
                    Text("Clinic Hours", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            when (activeSubTab) {
                0 -> {
                    // Search & Filter
                    OutlinedTextField(
                        value = adminState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search Patient Name or Booking ID...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    val filteredAppointments = appointments.filter { appt ->
                        val matchesSearch = appt.patientName.contains(adminState.searchQuery, ignoreCase = true) ||
                                appt.appointmentId.contains(adminState.searchQuery, ignoreCase = true)
                        val matchesStatus = if (adminState.selectedStatusFilter == "all") true else appt.status == adminState.selectedStatusFilter
                        matchesSearch && matchesStatus
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredAppointments) { appt ->
                            AdminAppointmentCard(
                                appointment = appt,
                                onUpdateStatus = { status -> onUpdateStatus(appt.appointmentId, status) }
                            )
                        }
                    }
                }
                1 -> {
                    // Block Time
                    var blockDate by remember { mutableStateOf("") }
                    var blockSlot by remember { mutableStateOf("ALL_DAY") }
                    var blockReason by remember { mutableStateOf("Clinic Maintenance") }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Block Time Slot or Date", fontWeight = FontWeight.Bold, color = NavyPrimary)
                            OutlinedTextField(
                                value = blockDate,
                                onValueChange = { blockDate = it },
                                label = { Text("Date (YYYY-MM-DD)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = blockSlot,
                                onValueChange = { blockSlot = it },
                                label = { Text("Time Slot (or ALL_DAY)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = blockReason,
                                onValueChange = { blockReason = it },
                                label = { Text("Reason") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (blockDate.isNotBlank()) {
                                        onBlockSlot(blockDate, blockSlot, blockReason)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = RedAccent)
                            ) {
                                Text("BLOCK SLOT", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Text("Currently Blocked Slots:", fontWeight = FontWeight.Bold, color = NavyPrimary)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(blockedSlots) { blocked ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SlateSurface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("${blocked.date} - ${blocked.timeSlot}", fontWeight = FontWeight.Bold, color = NavyPrimary)
                                        Text(blocked.reason, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                                    }
                                    IconButton(onClick = { onUnblockSlot(blocked) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Unblock", tint = RedAccent)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Clinic Hours Toggle
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(clinicHours) { hour ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SlateSurface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(hour.dayName, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                        Text(
                                            if (hour.isOpen) "${hour.openTime} - ${hour.closeTime}" else "Closed",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (hour.isOpen) StatusCompleted else RedAccent,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Switch(
                                        checked = hour.isOpen,
                                        onCheckedChange = { onToggleClinicDay(hour) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricChip(modifier: Modifier = Modifier, title: String, count: Int, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count.toString(), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = color)
            Text(title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF64748B)))
        }
    }
}

@Composable
fun AdminAppointmentCard(
    appointment: AppointmentEntity,
    onUpdateStatus: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(appointment.appointmentId, fontWeight = FontWeight.Bold, color = GoldDark)
                Surface(
                    color = when (appointment.status) {
                        "pending" -> StatusPending
                        "confirmed" -> StatusConfirmed
                        "completed" -> StatusCompleted
                        else -> RedAccent
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        appointment.status.uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Text("Patient: ${appointment.patientName} (${appointment.patientMobile})", fontWeight = FontWeight.Bold, color = NavyPrimary)
            Text("Service: ${appointment.serviceName}", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569)))
            Text("Date & Slot: ${appointment.date} at ${appointment.timeSlot}", style = MaterialTheme.typography.bodySmall.copy(color = NavyPrimary, fontWeight = FontWeight.SemiBold))

            Divider(color = SlateBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (appointment.status == "pending") {
                    Button(
                        onClick = { onUpdateStatus("confirmed") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusConfirmed),
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text("CONFIRM", fontSize = 10.sp)
                    }
                }
                if (appointment.status == "confirmed") {
                    Button(
                        onClick = { onUpdateStatus("completed") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusCompleted),
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text("COMPLETE", fontSize = 10.sp)
                    }
                }
                if (appointment.status != "cancelled" && appointment.status != "completed") {
                    OutlinedButton(
                        onClick = { onUpdateStatus("cancelled") },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text("CANCEL", fontSize = 10.sp, color = RedAccent)
                    }
                }
            }
        }
    }
}
