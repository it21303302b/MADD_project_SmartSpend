package com.example.smartspend

import com.google.firebase.database.DataSnapshot

data class ExpenceModel(
    var expenceId: String? = "",
    var expenceName: String? = "",
    var expenceDescription: String? = "",
    var expenceAmount: String? = "",
    var date: String? = "",
    var time: String? = ""
) {
    companion object {
        fun createFromSnapshot(snapshot: DataSnapshot): ExpenceModel {
            val expence = snapshot.getValue(ExpenceModel::class.java)
            expence!!.expenceId = snapshot.key
            return expence
        }
    }
}


