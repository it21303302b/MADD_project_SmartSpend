package com.example.smartspend

class User {
    var name: String = ""
    var gender: String = ""

    constructor() {
        // Required empty constructor for Firebase Realtime Database
    }

    constructor(name: String, gender: String) {
        this.name = name
        this.gender = gender
    }
}
