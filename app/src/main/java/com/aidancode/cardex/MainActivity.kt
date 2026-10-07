package com.aidancode.cardex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aidancode.cardex.ui.theme.CarDexTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CarDexTheme {
                CarDexApp()
            }
        }
    }
}

@Composable
fun CarDexApp() {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {

            NavigationBar(
                modifier = Modifier.navigationBarsPadding()
            ) {

                // HOME
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    },
                    icon = {
                        Text(
                            text = "🏠",
                            fontSize = 22.sp
                        )
                    },
                    label = {
                        Text("Home")
                    }
                )

                // CARDEX
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                    },
                    icon = {
                        Text(
                            text = "🚗",
                            fontSize = 22.sp
                        )
                    },
                    label = {
                        Text("CarDex")
                    }
                )

                // SPOT
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                    },
                    icon = {
                        Text(
                            text = "📸",
                            fontSize = 22.sp
                        )
                    },
                    label = {
                        Text("Spot")
                    }
                )

                // PROFILE
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                    },
                    icon = {
                        Text(
                            text = "👤",
                            fontSize = 22.sp
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
        }
    ) { innerPadding ->

        when (selectedTab) {

            // HOME
            0 -> {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            // CARDEX
            1 -> {
                PlaceholderScreen(
                    title = "CarDex",
                    subtitle = "Your collection will live here.",
                    icon = "🚗",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            // SPOT
            2 -> {
                PlaceholderScreen(
                    title = "Spot a Car",
                    subtitle = "Soon you'll be able to add cars you've spotted.",
                    icon = "📸",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            // PROFILE
            3 -> {
                PlaceholderScreen(
                    title = "Profile",
                    subtitle = "Your stats and achievements will live here.",
                    icon = "👤",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER
        item {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "CarDex",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Gotta spot 'em all.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }

                IconButton(
                    onClick = {
                        // Premium button - functionality later
                    }
                ) {

                    Text(
                        text = "⭐",
                        fontSize = 22.sp
                    )
                }
            }
        }

        // LEVEL
        item {
            LevelCard()
        }

        // DAILY CHALLENGE
        item {
            DailyChallengeCard()
        }

        // COLLECTION
        item {
            CollectionCard()
        }

        // RECENT SPOTS HEADER
        item {

            Text(
                text = "Recent Spots",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // RECENT SPOT 1
        item {

            RecentSpot(
                car = "BMW M3",
                details = "G80 • Rare"
            )
        }

        // RECENT SPOT 2
        item {

            RecentSpot(
                car = "Honda Civic",
                details = "11th Generation • Common"
            )
        }

        // RECENT SPOT 3
        item {

            RecentSpot(
                car = "Ford Mustang",
                details = "S650 • Uncommon"
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
fun LevelCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "LEVEL 1",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Car Collector",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "0 XP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // XP PROGRESS BAR

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.05f)
                        .height(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "100 XP needed for Level 2",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DailyChallengeCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "🎯  TODAY'S CHALLENGE",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Spot a BMW",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Find a BMW anywhere in the real world.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "+50 XP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Button(
                    onClick = {
                        // Challenge functionality later
                    }
                ) {

                    Text("Start")
                }
            }
        }
    }
}

@Composable
fun CollectionCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "YOUR COLLECTION",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "0 / 500",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "cars discovered",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // COLLECTION PROGRESS BAR

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
    }
}

@Composable
fun RecentSpot(
    car: String,
    details: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(52.dp),

                shape = RoundedCornerShape(14.dp),

                color = MaterialTheme.colorScheme.surfaceVariant
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "🚗",
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {

                Text(
                    text = car,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Text(
                    text = details,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = icon,
            fontSize = 70.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = subtitle,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}