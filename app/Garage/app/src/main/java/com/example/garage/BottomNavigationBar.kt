package com.example.garage

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onNewOrderClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onHomeClick,
            label = { Text("Cennik") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_list),  // Upewnij się, że `ic_list` jest poprawnie dodane w res/drawable/
                    contentDescription = "Cennik"
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onNewOrderClick,
            label = { Text("Nowe Zlecenie") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_add),  // Ikona dodawania
                    contentDescription = "Nowe Zlecenie"
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onHistoryClick,
            label = { Text("Historia") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_history),  // Używamy poprawionej ikony zegara
                    contentDescription = "Historia Zleceń"
                )
            }
        )
    }
}
