package com.example.testeverything.singleselection

data class SelectableItem(
    val id: Int,
    val title: String,
    val description: String,
    val isSelected: Boolean = false
)
