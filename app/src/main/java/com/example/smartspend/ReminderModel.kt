package com.example.smartspend

import com.google.firebase.database.DataSnapshot
import java.util.Date

data class ReminderModel (

    var remId: String? = null,
    var reminderDes: String? = null,
    var reminderDate: String? = null,
    var reminderAmount: String? = null,
    var reminderType: String? = null

    ){

    companion object {
        fun createFromSnapshot(snapshot: DataSnapshot): ReminderModel {
            val expence = snapshot.getValue(ReminderModel::class.java)
            expence!!.remId = snapshot.key
            return expence
        }
    }
}