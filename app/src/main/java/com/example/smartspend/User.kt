package com.example.smartspend

class User {
    var name: String = ""
    var age: String = ""
    var occupation: String = ""

    constructor() {
        // Required empty constructor for Firebase Realtime Database
    }

    constructor(name: String, age: String, occupation: String) {
        this.name = name
        this.age = age
        this.occupation = occupation
    }
}