package com.example.smartspend

data class Category(val category: String? = null, val description: String? = null, val categoryID: String = "") {
    constructor() : this("", "")
}

