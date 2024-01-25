package com.example.vaccinemanager

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RequirementsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnBackHome: Button
    private val countryList = arrayOf(
        "Australia",
        "Brazil",
        "Cambodia",
        "Costa Rica",
        "Egypt",
        "India",
        "Indonesia (Bali)",
        "Kenya",
        "Morocco",
        "Nepal",
        "Peru",
        "South Africa",
        "Tanzania",
        "Thailand",
        "Vietnam",
        "Argentina",
        "Bolivia",
        "Chile",
        "Ecuador",
        "Fiji",
        "Ghana",
        "Japan",
        "Malaysia",
        "Mexico",
        "New Zealand"
    )

    private val countryReqMap = mapOf(
        "Australia" to R.string.australia_info,
        "Brazil" to R.string.brazil_info,
        "Cambodia" to R.string.cambodia_info,
        "Costa Rica" to R.string.costa_rica_info,
        "Egypt" to R.string.egypt_info,
        "India" to R.string.india_info,
        "Indonesia (Bali)" to R.string.indonesia_info,
        "Kenya" to R.string.kenya_info,
        "Morocco" to R.string.morocco_info,
        "Nepal" to R.string.nepal_info,
        "Peru" to R.string.peru_info,
        "South Africa" to R.string.south_africa_info,
        "Tanzania" to R.string.tanzania_info,
        "Thailand" to R.string.thailand_info,
        "Vietnam" to R.string.vietnam_info,
        "Argentina" to R.string.argentina_info,
        "Bolivia" to R.string.bolivia_info,
        "Chile" to R.string.chile_info,
        "Ecuador" to R.string.ecuador_info,
        "Fiji" to R.string.fiji_info,
        "Ghana" to R.string.ghana_info,
        "Japan" to R.string.japan_info,
        "Malaysia" to R.string.malaysia_info,
        "Mexico" to R.string.mexico_info,
        "New Zealand" to R.string.new_zealand_info
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requirements)

        btnBackHome = findViewById(R.id.btnBackHome2)

        btnBackHome.setOnClickListener {
            startActivity(
                Intent(this@RequirementsActivity,
                    HomeActivity::class.java)
            )
        }

        listView = findViewById(R.id.listViewVaccines)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countryList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countryList[position]
            showTravelRequirementsDialog(selectedCountry)
        }
    }

    private fun showTravelRequirementsDialog(countryName: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_travel_requirements)

        val tvCountryName = dialog.findViewById<TextView>(R.id.tvCountryName)
        val tvRequirementsInfo = dialog.findViewById<TextView>(R.id.tvRequirementsInfo)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        tvCountryName.text = countryName

        val requirementsInfo = countryReqMap[countryName]  ?: R.string.default_country_info
        tvRequirementsInfo.text = getString(requirementsInfo)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
