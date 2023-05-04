package com.example.smartspend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class UpdateExpence : AppCompatActivity() {

    private lateinit var tvShowCurrentExpence: TextView
    private lateinit var tvCurrentDescriptionExp: TextView
    private lateinit var tvExpIdshow: TextView
    private lateinit var tvExpAmounVal: TextView
    private lateinit var btnUpdate : Button
    private lateinit var btnDeleteCategory : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_expence)

        initView()
        setValuesToViews()
    }

    private fun initView() {
        tvExpIdshow = findViewById(R.id.tvExpIdshow)
        tvShowCurrentExpence = findViewById(R.id.tvShowCurrentExpence)
        tvCurrentDescriptionExp = findViewById(R.id.tvCurrentDescriptionExp)
        tvExpAmounVal = findViewById(R.id.tvExpAmounVal)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory)
    }


    private fun setValuesToViews() {
        tvExpIdshow.text = intent.getStringExtra("expenceId")
        tvShowCurrentExpence.text = intent.getStringExtra("expenceName")
        tvCurrentDescriptionExp.text = intent.getStringExtra("expenceDescription")
        tvExpAmounVal.text = intent.getStringExtra("expenceAmount")
    }


}