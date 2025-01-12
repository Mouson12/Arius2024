package com.example.garage

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

/**
 * A composable function for displaying a bottom navigation bar with three items:
 * - "Cennik" (Price List)
 * - "Nowe Zlecenie" (New Order)
 * - "Historia" (History)
 *
 * Each item triggers a callback when clicked.
 *
 * @param onHomeClick Callback for the "Cennik" item.
 * @param onNewOrderClick Callback for the "Nowe Zlecenie" item.
 * @param onHistoryClick Callback for the "Historia" item.
 */
@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onNewOrderClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    // Creates a navigation bar with three items
    NavigationBar {
        // Navigation item for "Cennik" (Price List)
        NavigationBarItem(
            selected = false, // Selection state (can be linked to navigation state)
            onClick = onHomeClick, // Callback triggered on click
            label = { Text("Cennik") }, // Label for the item
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_list), // Icon resource for "Cennik"
                    contentDescription = "Cennik" // Accessibility description
                )
            }
        )

        // Navigation item for "Nowe Zlecenie" (New Order)
        NavigationBarItem(
            selected = false,
            onClick = onNewOrderClick,
            label = { Text("Nowe Zlecenie") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_add), // Icon resource for "New Order"
                    contentDescription = "Nowe Zlecenie" // Accessibility description
                )
            }
        )

        // Navigation item for "Historia" (History)
        NavigationBarItem(
            selected = false,
            onClick = onHistoryClick,
            label = { Text("Historia") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_history), // Icon resource for "History"
                    contentDescription = "Historia Zleceń" // Accessibility description
                )
            }
        )
    }
}