package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    val database = Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(initial = emptyList())

    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val firstName = sharedPreferences.getString("firstName", "") ?: ""
    val lastName = sharedPreferences.getString("lastName", "") ?: ""
    val initials = buildString {
        if (firstName.isNotBlank()) append(firstName.first().uppercase())
        if (lastName.isNotBlank()) append(lastName.first().uppercase())
    }.ifBlank { "LL" }

    var searchPhrase by remember { mutableStateOf("") }
    var starterSelected by remember { mutableStateOf(false) }
    var mainSelected by remember { mutableStateOf(false) }
    var dessertSelected by remember { mutableStateOf(false) }
    var drinkSelected by remember { mutableStateOf(false) }

    val filteredItems = databaseMenuItems.filter { item ->
        val matchesSearch = searchPhrase.isBlank() || item.title.contains(searchPhrase, ignoreCase = true)
        val matchesCategory = when {
            !starterSelected && !mainSelected && !dessertSelected && !drinkSelected -> true
            else -> {
                (starterSelected && item.category.equals("starters", ignoreCase = true)) ||
                (mainSelected && item.category.equals("mains", ignoreCase = true)) ||
                (dessertSelected && item.category.equals("desserts", ignoreCase = true)) ||
                (drinkSelected && item.category.equals("drinks", ignoreCase = true))
            }
        }
        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(40.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.height(40.dp)
            )
            // Profile Initials Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF495E57))
                    .clickable { navController.navigate("Profile") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        // Hero Section (Green Card)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF495E57), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Little Lemon",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF4CE14)
            )
            Text(
                text = "Chicago",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Hero Image",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchPhrase,
                onValueChange = { searchPhrase = it },
                placeholder = { Text("Enter search phrase", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_search),
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    focusedBorderColor = Color(0xFFF4CE14),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF495E57)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ORDER FOR DELIVERY!",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Category Filter Buttons (Horizontally Scrollable Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { starterSelected = !starterSelected },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (starterSelected) Color(0xFF495E57) else Color(0xFFEDEFEE)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Starters", color = if (starterSelected) Color.White else Color(0xFF333333), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Button(
                onClick = { mainSelected = !mainSelected },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (mainSelected) Color(0xFF495E57) else Color(0xFFEDEFEE)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Mains", color = if (mainSelected) Color.White else Color(0xFF333333), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Button(
                onClick = { dessertSelected = !dessertSelected },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (dessertSelected) Color(0xFF495E57) else Color(0xFFEDEFEE)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Desserts", color = if (dessertSelected) Color.White else Color(0xFF333333), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Button(
                onClick = { drinkSelected = !drinkSelected },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (drinkSelected) Color(0xFF495E57) else Color(0xFFEDEFEE)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Drinks", color = if (drinkSelected) Color.White else Color(0xFF333333), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        // Menu Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(
                items = filteredItems,
                itemContent = { menuItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = menuItem.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF333333)
                            )
                            Text(
                                text = "Traditional Mediterranean recipe with a modern twist.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "$%.2f".format(menuItem.price),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF495E57)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Item Image",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .padding(start = 8.dp)
                        )
                    }
                    Divider(color = Color(0xFFEDEFEE), thickness = 1.dp)
                }
            )
        }
    }
}
