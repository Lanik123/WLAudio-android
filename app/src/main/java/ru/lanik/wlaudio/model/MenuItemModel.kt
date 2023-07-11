package ru.lanik.wlaudio.model

data class MenuItemModel(
    val title: String,
    val currentIndex: Int = 0,
    val dropdownMenuModel: DropdownMenuModel,
)

data class DropdownMenuModel(
    val values: List<String>,
)