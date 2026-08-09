package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.NotificationManager

data class FaqItem(val question: String, val answer: String)

@Composable
fun ContactAndMapScreen(
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val faqs = remember {
        listOf(
            FaqItem(
                question = "Do I need a doctor's referral for spine physiotherapy?",
                answer = "No, you can book an appointment directly. Dr. Charles P. Joseph conducts a full clinical evaluation during your first consultation."
            ),
            FaqItem(
                question = "What should I bring for my first consultation?",
                answer = "Please bring any recent Spine X-Rays, MRI reports, or medical prescriptions if available."
            ),
            FaqItem(
                question = "Do you provide home physiotherapy visits in Vellore?",
                answer = "Yes! Home physiotherapy visits are available for acute spine pain, post-surgical rehabilitation, and geriatric care at ₹650–₹750 per session."
            ),
            FaqItem(
                question = "What are the clinic operating hours?",
                answer = "The clinic is open Monday to Friday from 7:30 AM to 12:30 AM midnight. Saturday and Sunday are closed."
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // --- CLINIC ADDRESS & DIRECT ACTION CARD ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "CHARLIE'S SPINE KLINIC",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = RedAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "Home Clinic #7/32, Jehovah Jireh House,",
                                fontWeight = FontWeight.SemiBold,
                                color = NavyPrimary
                            )
                            Text(
                                text = "Auxilium College Road, Gandhinagar, Vellore – 632006, Tamil Nadu",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569))
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = StatusCompleted,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "+91-90822-49505",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyPrimary
                            )
                        )
                    }

                    Divider(color = SlateBorder)

                    Button(
                        onClick = onMapClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = GoldAccent
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Directions, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPEN IN GOOGLE MAPS", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- FREQUENTLY ASKED QUESTIONS (FAQ) ---
        item {
            Text(
                text = "Frequently Asked Questions (FAQ)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
            )
        }

        items(faqs) { faq ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = faq.question,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = NavyPrimary
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Text(
                            text = faq.answer,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF475569),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
