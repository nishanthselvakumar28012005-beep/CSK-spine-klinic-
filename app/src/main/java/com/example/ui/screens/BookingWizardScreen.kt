package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.BookingFormState
import com.example.util.NotificationManager
import com.example.util.PaymentMethod
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookingWizardScreen(
    bookingState: BookingFormState,
    services: List<ServiceEntity>,
    onSelectService: (ServiceEntity) -> Unit,
    onDateSelected: (String) -> Unit,
    onSlotSelected: (String) -> Unit,
    onUpdatePatientDetails: (String, String, String, String, String) -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onStepChange: (Int) -> Unit,
    onSubmitBooking: () -> Unit,
    onResetBooking: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Step Progress Indicator Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "APPOINTMENT BOOKING",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Step ${bookingState.step} of 6",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                LinearProgressIndicator(
                    progress = bookingState.step / 6f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = GoldAccent,
                    trackColor = NavyLight
                )

                val stepTitle = when (bookingState.step) {
                    1 -> "1. Select Clinical Service"
                    2 -> "2. Choose Consultation Date"
                    3 -> "3. Select Available Time Slot"
                    4 -> "4. Patient Details & Consultation Reason"
                    5 -> "5. Review & Payment Method"
                    else -> "6. Appointment Confirmed"
                }

                Text(
                    text = stepTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        // Error Banner
        bookingState.errorMessage?.let { error ->
            Surface(
                color = RedLight,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, RedAccent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = RedAccent)
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = RedAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Wizard Step Content
        Box(modifier = Modifier.weight(1f)) {
            when (bookingState.step) {
                1 -> Step1SelectService(services, onSelectService)
                2 -> Step2SelectDate(
                    selectedDate = bookingState.selectedDate,
                    onDateSelected = onDateSelected,
                    onNext = { onStepChange(3) }
                )
                3 -> Step3SelectSlot(
                    availableSlots = bookingState.availableSlots,
                    selectedSlot = bookingState.selectedSlot,
                    onSlotSelected = onSlotSelected,
                    onNext = { onStepChange(4) },
                    onBack = { onStepChange(2) }
                )
                4 -> Step4PatientDetails(
                    name = bookingState.name,
                    mobile = bookingState.mobile,
                    email = bookingState.email,
                    age = bookingState.age,
                    reason = bookingState.reason,
                    onUpdate = onUpdatePatientDetails,
                    onNext = { onStepChange(5) },
                    onBack = { onStepChange(3) }
                )
                5 -> Step5SummaryAndPayment(
                    bookingState = bookingState,
                    onPaymentMethodSelected = onPaymentMethodSelected,
                    onSubmitBooking = onSubmitBooking,
                    onBack = { onStepChange(4) }
                )
                6 -> Step6Confirmation(
                    appointment = bookingState.confirmedAppointment,
                    onWhatsAppNotify = {
                        bookingState.confirmedAppointment?.let { appt ->
                            NotificationManager.openWhatsAppBookingMessage(
                                context,
                                appt.appointmentId,
                                appt.serviceName,
                                appt.date,
                                appt.timeSlot,
                                appt.patientName
                            )
                        }
                    },
                    onNewBooking = onResetBooking
                )
            }
        }
    }
}

// --- STEP 1: SELECT SERVICE ---
@Composable
fun Step1SelectService(
    services: List<ServiceEntity>,
    onSelectService: (ServiceEntity) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(services) { service ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectService(service) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                        Text(
                            text = "${service.durationMinutes} mins • ${service.category}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }
                    Text(
                        text = "₹${service.priceMin}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldDark
                        )
                    )
                }
            }
        }
    }
}

// --- STEP 2: SELECT DATE ---
@Composable
fun Step2SelectDate(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displaySdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())

    // Generate next 14 available dates
    val calendar = Calendar.getInstance()
    val dateList = remember {
        val list = mutableListOf<Pair<String, String>>()
        for (i in 0..13) {
            val dateStr = sdf.format(calendar.time)
            val labelStr = displaySdf.format(calendar.time)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                list.add(Pair(dateStr, labelStr))
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Select an available clinic day (Mon-Fri):",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = NavyPrimary
            )
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(dateList) { (dateCode, dateLabel) ->
                val isSelected = dateCode == selectedDate
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDateSelected(dateCode) },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NavyPrimary else SlateSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) GoldAccent else SlateBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) GoldAccent else NavyPrimary
                            )
                        )
                        if (isSelected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = GoldAccent)
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
        ) {
            Text("PROCEED TO TIME SLOTS", fontWeight = FontWeight.Bold)
        }
    }
}

// --- STEP 3: SELECT TIME SLOT ---
@Composable
fun Step3SelectSlot(
    availableSlots: List<String>,
    selectedSlot: String?,
    onSlotSelected: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (availableSlots.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.EventBusy, contentDescription = null, tint = RedAccent, modifier = Modifier.size(36.dp))
                    Text(
                        text = "No Slots Available for Selected Date",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                    )
                    Text(
                        text = "The clinic may be closed, fully booked or blocked by admin.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                    )
                }
            }
        } else {
            Text(
                text = "Available 30-min time slots:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = NavyPrimary)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(availableSlots) { slot ->
                    val isSelected = slot == selectedSlot
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSlotSelected(slot) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) NavyPrimary else SlateSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) GoldAccent else SlateBorder
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = slot,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GoldAccent else NavyPrimary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("BACK")
            }

            Button(
                onClick = onNext,
                enabled = !selectedSlot.isNull_or_empty(),
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
            ) {
                Text("ENTER DETAILS", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- STEP 4: PATIENT DETAILS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4PatientDetails(
    name: String,
    mobile: String,
    email: String,
    age: String,
    reason: String,
    onUpdate: (String, String, String, String, String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var nameState by remember { mutableStateOf(name) }
    var mobileState by remember { mutableStateOf(mobile) }
    var emailState by remember { mutableStateOf(email) }
    var ageState by remember { mutableStateOf(age) }
    var reasonState by remember { mutableStateOf(reason) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = nameState,
            onValueChange = {
                nameState = it
                onUpdate(nameState, mobileState, emailState, ageState, reasonState)
            },
            label = { Text("Full Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = mobileState,
            onValueChange = {
                mobileState = it
                onUpdate(nameState, mobileState, emailState, ageState, reasonState)
            },
            label = { Text("Mobile Phone Number (+91) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = emailState,
            onValueChange = {
                emailState = it
                onUpdate(nameState, mobileState, emailState, ageState, reasonState)
            },
            label = { Text("Email Address (Optional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = ageState,
            onValueChange = {
                ageState = it
                onUpdate(nameState, mobileState, emailState, ageState, reasonState)
            },
            label = { Text("Age (Years)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = reasonState,
            onValueChange = {
                reasonState = it
                onUpdate(nameState, mobileState, emailState, ageState, reasonState)
            },
            label = { Text("Chief Complaint / Spine Pain Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            maxLines = 3
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("BACK")
            }

            Button(
                onClick = onNext,
                enabled = nameState.isNotBlank() && mobileState.isNotBlank(),
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
            ) {
                Text("REVIEW APPOINTMENT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- STEP 5: SUMMARY & PAYMENT ---
@Composable
fun Step5SummaryAndPayment(
    bookingState: BookingFormState,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onSubmitBooking: () -> Unit,
    onBack: () -> Unit
) {
    val service = bookingState.selectedService

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("APPOINTMENT SUMMARY", fontWeight = FontWeight.Bold, color = NavyPrimary, fontSize = 12.sp)
                Divider(color = SlateBorder)
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Service:", color = Color(0xFF64748B))
                    Text(service?.name ?: "", fontWeight = FontWeight.Bold, color = NavyPrimary)
                }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Date & Time:", color = Color(0xFF64748B))
                    Text("${bookingState.selectedDate} at ${bookingState.selectedSlot}", fontWeight = FontWeight.Bold, color = NavyPrimary)
                }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Patient:", color = Color(0xFF64748B))
                    Text("${bookingState.name} (${bookingState.mobile})", fontWeight = FontWeight.Bold, color = NavyPrimary)
                }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Total Consultation Fee:", color = Color(0xFF64748B))
                    Text("₹${service?.priceMin}", fontWeight = FontWeight.ExtraBold, color = GoldDark, fontSize = 16.sp)
                }
            }
        }

        Text("Select Payment Preference:", fontWeight = FontWeight.Bold, color = NavyPrimary)

        PaymentMethod.values().forEach { method ->
            val isSelected = method == bookingState.paymentMethod
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPaymentMethodSelected(method) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) NavyPrimary else SlateSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) GoldAccent else SlateBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onPaymentMethodSelected(method) },
                        colors = RadioButtonDefaults.colors(selectedColor = GoldAccent)
                    )
                    Text(
                        text = method.displayName,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else NavyPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("BACK")
            }

            Button(
                onClick = onSubmitBooking,
                enabled = !bookingState.isSubmitting,
                modifier = Modifier
                    .weight(1.8f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
            ) {
                if (bookingState.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = NavyPrimary)
                } else {
                    Text("CONFIRM APPOINTMENT", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

// --- STEP 6: CONFIRMATION ---
@Composable
fun Step6Confirmation(
    appointment: com.example.data.model.AppointmentEntity?,
    onWhatsAppNotify: () -> Unit,
    onNewBooking: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = StatusCompleted
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
            }
        }

        Text(
            text = "Appointment Reserved!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
        )

        appointment?.let { appt ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("BOOKING ID:", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Text(appt.appointmentId, fontWeight = FontWeight.ExtraBold, color = GoldDark, fontSize = 16.sp)
                    }
                    Divider(color = SlateBorder)
                    Text("Service: ${appt.serviceName}", fontWeight = FontWeight.Bold, color = NavyPrimary)
                    Text("Date & Time: ${appt.date} at ${appt.timeSlot}", fontWeight = FontWeight.SemiBold, color = NavyPrimary)
                    Text("Patient: ${appt.patientName} (${appt.patientMobile})", color = NavyPrimary)
                    Text("Status: ${appt.status.uppercase()}", fontWeight = FontWeight.Bold, color = StatusPending)
                }
            }
        }

        Button(
            onClick = onWhatsAppNotify,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusCompleted, contentColor = Color.White)
        ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SEND CONFIRMATION TO WHATSAPP", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onNewBooking,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("BOOK ANOTHER APPOINTMENT")
        }
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
