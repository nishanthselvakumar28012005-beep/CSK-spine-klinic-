package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.KlinicTopAppBar
import com.example.ui.components.StickyBottomActionBar
import com.example.ui.screens.*
import com.example.ui.theme.SpineKlinicTheme
import com.example.ui.viewmodel.KlinicViewModel
import com.example.util.NotificationManager

class MainActivity : ComponentActivity() {

    private val viewModel: KlinicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpineKlinicTheme {
                KlinicAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KlinicAppContent(viewModel: KlinicViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val services by viewModel.allServices.collectAsStateWithLifecycle()
    val clinicHours by viewModel.clinicHours.collectAsStateWithLifecycle()
    val blockedSlots by viewModel.blockedSlots.collectAsStateWithLifecycle()
    val appointments by viewModel.allAppointments.collectAsStateWithLifecycle()
    val articles by viewModel.articles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            KlinicTopAppBar(
                currentTab = uiState.currentTab,
                onAdminClick = { viewModel.selectTab(5) },
                onPhoneClick = { NotificationManager.makePhoneCall(context) },
                onWhatsAppClick = { NotificationManager.openWhatsAppDirect(context) }
            )
        },
        bottomBar = {
            StickyBottomActionBar(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                onCallClick = { NotificationManager.makePhoneCall(context) },
                onWhatsAppClick = { NotificationManager.openWhatsAppDirect(context) },
                onBookClick = { viewModel.selectTab(2) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                0 -> HomeScreen(
                    onBookClick = { viewModel.selectTab(2) },
                    onCallClick = { NotificationManager.makePhoneCall(context) },
                    onWhatsAppClick = { NotificationManager.openWhatsAppDirect(context) },
                    onViewTariffClick = { viewModel.selectTab(1) }
                )
                1 -> ServicesScreen(
                    services = services,
                    onSelectService = { service ->
                        viewModel.selectServiceForBooking(service)
                        viewModel.selectTab(2)
                    }
                )
                2 -> BookingWizardScreen(
                    bookingState = uiState.bookingState,
                    services = services,
                    onSelectService = { viewModel.selectServiceForBooking(it) },
                    onDateSelected = { viewModel.setBookingDate(it) },
                    onSlotSelected = { viewModel.selectBookingSlot(it) },
                    onUpdatePatientDetails = { name, mobile, email, age, reason ->
                        viewModel.updatePatientDetails(name, mobile, email, age, reason)
                    },
                    onPaymentMethodSelected = { viewModel.setPaymentMethod(it) },
                    onStepChange = { viewModel.setBookingStep(it) },
                    onSubmitBooking = { viewModel.submitBooking() },
                    onResetBooking = { viewModel.resetBookingForm() }
                )
                3 -> KnowledgeCenterScreen(articles = articles)
                4 -> ContactAndMapScreen(
                    onCallClick = { NotificationManager.makePhoneCall(context) },
                    onWhatsAppClick = { NotificationManager.openWhatsAppDirect(context) },
                    onMapClick = { NotificationManager.openGoogleMapsLocation(context) }
                )
                5 -> AdminScreen(
                    adminState = uiState.adminState,
                    appointments = appointments,
                    services = services,
                    clinicHours = clinicHours,
                    blockedSlots = blockedSlots,
                    onLoginPin = { pin -> viewModel.loginAdmin(pin) },
                    onLogout = { viewModel.logoutAdmin() },
                    onUpdateStatus = { id, status -> viewModel.updateAppointmentStatus(id, status) },
                    onBlockSlot = { date, slot, reason -> viewModel.blockTimeSlot(date, slot, reason) },
                    onUnblockSlot = { slot -> viewModel.unblockTimeSlot(slot) },
                    onToggleClinicDay = { hour -> viewModel.toggleClinicDayStatus(hour) },
                    onSearchQueryChange = { viewModel.setAdminSearchQuery(it) },
                    onFilterStatusChange = { viewModel.setAdminFilterStatus(it) }
                )
            }
        }
    }
}
