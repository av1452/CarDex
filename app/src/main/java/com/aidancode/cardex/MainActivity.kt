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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aidancode.cardex.model.Car
import com.aidancode.cardex.model.Rarity
import com.aidancode.cardex.ui.theme.CarDexTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.OutlinedTextField
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import coil3.compose.AsyncImage
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

    val context = LocalContext.current

    val storage = remember {
        CarStorage(context)
    }

    val scope = rememberCoroutineScope()

    val cars = remember {
        mutableStateListOf<Car>()
    }

    // Load saved cars when the app starts
    LaunchedEffect(Unit) {

        storage.cars.collect { savedCars ->

            cars.clear()
            cars.addAll(savedCars)
        }
    }

    val totalXp = cars.sumOf { it.xp }

    Scaffold(
        bottomBar = {

            NavigationBar(
                modifier = Modifier.navigationBarsPadding()
            ) {

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

            0 -> {
                HomeScreen(
                    cars = cars,
                    totalXp = totalXp,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            1 -> {
                CarDexScreen(
                    cars = cars,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            2 -> {
                SpotScreen(
                    onCarAdded = { car ->

                        cars.add(car)

                        scope.launch {
                            storage.saveCars(cars.toList())
                        }

                        selectedTab = 1
                    },
                    nextId = cars.size + 1,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            3 -> {
                ProfileScreen(
                    cars = cars,
                    totalXp = totalXp,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun CarDexScreen(
    cars: List<Car>,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "My CarDex",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${cars.size} cars discovered",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(cars) { car ->

            CarCard(
                car = car
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
fun CarCard(
    car: Car,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {

            if (car.photoUri.isNotEmpty()) {
                AsyncImage(
                    model = car.photoUri,
                    contentDescription = "${car.manufacturer} ${car.model}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🚗",
                            fontSize = 56.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "${car.manufacturer} ${car.model}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${car.generation} • ${car.year} • ${car.trim}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = car.rarity.name,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "+${car.xp} XP",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SpotScreen(
    onCarAdded: (Car) -> Unit,
    nextId: Int,
    modifier: Modifier = Modifier
) {

    var manufacturer by remember {
        mutableStateOf("")
    }

    var model by remember {
        mutableStateOf("")
    }

    var generation by remember {
        mutableStateOf("")
    }

    var year by remember {
        mutableStateOf("2026")
    }

    var trim by remember {
        mutableStateOf("")
    }

    var rarity by remember {
        mutableStateOf(Rarity.COMMON)
    }

    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            photoUri = uri
        }

    val xp = xpForRarity(rarity)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Spot a Car",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Add a car you've spotted to your CarDex.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {

            OutlinedTextField(
                value = manufacturer,
                onValueChange = {
                    manufacturer = it
                },
                label = {
                    Text("Manufacturer")
                },
                placeholder = {
                    Text("BMW")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {

            OutlinedTextField(
                value = model,
                onValueChange = {
                    model = it
                },
                label = {
                    Text("Model")
                },
                placeholder = {
                    Text("M5")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {

            OutlinedTextField(
                value = generation,
                onValueChange = {
                    generation = it
                },
                label = {
                    Text("Generation")
                },
                placeholder = {
                    Text("G90")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {

            OutlinedTextField(
                value = year,
                onValueChange = { newValue ->

                    if (
                        newValue.all { character ->
                            character.isDigit()
                        } &&
                        newValue.length <= 4
                    ) {
                        year = newValue
                    }
                },
                label = {
                    Text("Year")
                },
                placeholder = {
                    Text("2026")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        item {

            OutlinedTextField(
                value = trim,
                onValueChange = {
                    trim = it
                },
                label = {
                    Text("Trim")
                },
                placeholder = {
                    Text("Competition")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {

            Button(
                onClick = {

                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = if (photoUri == null) {
                        "📷 Choose Photo"
                    } else {
                        "📷 Change Photo"
                    }
                )
            }
        }

        item {

            if (photoUri != null) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Car photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {

            DropdownSelector(
                title = "Rarity",
                selected = rarity.name
                    .lowercase()
                    .replaceFirstChar {
                        it.uppercase()
                    },
                options = Rarity.entries.map {
                    it.name
                        .lowercase()
                        .replaceFirstChar { character ->
                            character.uppercase()
                        }
                },
                onSelected = { selected ->

                    rarity = Rarity.valueOf(
                        selected.uppercase()
                    )
                }
            )
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Spot Preview",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = if (
                            manufacturer.isNotBlank() ||
                            model.isNotBlank()
                        ) {
                            "${year.ifBlank { "Year" }} " +
                                    "${manufacturer.ifBlank { "Manufacturer" }} " +
                                    model.ifBlank { "Model" }
                        } else {
                            "Your spotted car"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (generation.isNotBlank()) {

                        Text(
                            text = generation,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (trim.isNotBlank()) {

                        Text(
                            text = trim,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "${rarity.name.lowercase().replaceFirstChar { it.uppercase() }} • +$xp XP",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {

            Button(
                onClick = {

                    val newCar = Car(
                        id = nextId,

                        manufacturer = manufacturer,

                        model = model,

                        generation = generation.ifEmpty {
                            "Unknown"
                        },

                        year = year.toIntOrNull()
                            ?: 2026,

                        trim = trim.ifEmpty {
                            "Unknown"
                        },

                        rarity = rarity,

                        xp = xp,

                        dateSpotted =
                            System.currentTimeMillis(),

                        photoUri = photoUri?.toString() ?: ""
                    )

                    onCarAdded(newCar)

                    manufacturer = ""
                    model = ""
                    generation = ""
                    year = "2026"
                    trim = ""
                    rarity = Rarity.COMMON
                    photoUri = null
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                enabled =
                    manufacturer.isNotBlank() &&
                            model.isNotBlank()
            ) {

                Text(
                    text = "Add to CarDex",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
fun DropdownSelector(
    title: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Column {

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = if (selected.isEmpty()) {
                            "Select $title"
                        } else {
                            selected
                        }
                    )

                    Text("▼")
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                options.forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {

                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun xpForRarity(
    rarity: Rarity
): Int {

    return when (rarity) {

        Rarity.COMMON -> 10

        Rarity.UNCOMMON -> 25

        Rarity.RARE -> 50

        Rarity.EPIC -> 100

        Rarity.LEGENDARY -> 250
    }
}

@Composable
fun HomeScreen(
    cars: List<Car>,
    totalXp: Int,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                        // Premium functionality later
                    }
                ) {

                    Text(
                        text = "⭐",
                        fontSize = 22.sp
                    )
                }
            }
        }

        item {

            LevelCard(
                totalXp = totalXp
            )
        }

        item {

            DailyChallengeCard()
        }

        item {

            CollectionCard(
                collectionSize = cars.size
            )
        }

        item {

            Text(
                text = "Recent Spots",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(cars.takeLast(3).reversed()) { car ->

            RecentSpot(
                car = "${car.manufacturer} ${car.model}",
                details = "${car.generation} • ${car.rarity.name}"
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
fun LevelCard(
    totalXp: Int
) {

    val level = (totalXp / 100) + 1
    val xpIntoLevel = totalXp % 100

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
                        text = "LEVEL $level",
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
                    text = "$totalXp XP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

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
                        .fillMaxWidth(xpIntoLevel / 100f)
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
                text = "${100 - xpIntoLevel} XP needed for Level ${level + 1}",
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
fun CollectionCard(
    collectionSize: Int
) {

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
                text = "$collectionSize / 500",
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(
                            (collectionSize / 500f).coerceAtMost(1f)
                        )
                        .height(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
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
fun ProfileScreen(
    cars: List<Car>,
    totalXp: Int,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = "👤",
            fontSize = 70.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Car Collector",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            StatItem(
                value = cars.size.toString(),
                label = "Cars"
            )

            StatItem(
                value = totalXp.toString(),
                label = "XP"
            )

            StatItem(
                value = cars.count {
                    it.rarity == Rarity.LEGENDARY
                }.toString(),
                label = "Legendary"
            )
        }
    }
}

@Composable
fun StatItem(
    value: String,
    label: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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