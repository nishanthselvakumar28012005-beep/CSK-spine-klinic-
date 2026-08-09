package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArticleEntity
import com.example.ui.theme.*

@Composable
fun KnowledgeCenterScreen(
    articles: List<ArticleEntity>
) {
    var expandedArticleId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            color = NavyPrimary,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Spine Health Knowledge Centre",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Clinical guides on spinal rehabilitation, posture care & pain prevention.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFCBD5E1))
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                val isExpanded = expandedArticleId == article.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedArticleId = if (isExpanded) null else article.id
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = article.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GoldDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Text(
                                text = "${article.readTimeMinutes} min read • ${article.datePublished}",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }

                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )

                        Text(
                            text = article.summary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF475569),
                                fontSize = 13.sp
                            )
                        )

                        if (isExpanded) {
                            Divider(color = SlateBorder)
                            Text(
                                text = article.content,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = NavyPrimary,
                                    lineHeight = 20.sp
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { expandedArticleId = if (isExpanded) null else article.id }) {
                                Text(
                                    text = if (isExpanded) "Show Less" else "Read Full Guide",
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = GoldDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
