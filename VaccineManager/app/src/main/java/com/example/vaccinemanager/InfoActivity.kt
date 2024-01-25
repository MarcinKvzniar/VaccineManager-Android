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

class InfoActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnBackHome: Button
    private val vaccineList = arrayOf(
        "COVID-19 mRNA Vaccine",
        "COVID-19 protein subunit Vaccine",
        "BCG - Tuberculosis",
        "Hepatitis B Vaccine",
        "Poliovirus Vaccine",
        "DTP - Diphtheria Vaccine",
        "DTP - Tetanus Vaccine",
        "DTP - Pertussis Vaccine",
        "Hib - Haemophilus influenza type b",
        "Pneumococcal Vaccine",
        "Rotavirus Vaccine",
        "MMR - Measles Vaccine",
        "MMR - Mumps Vaccine",
        "MMR - Rubella Vaccine",
        "HPV Vaccine"
    )

    private val vaccineInfoMap = mapOf(
        "COVID-19 mRNA Vaccine" to R.string.covid_mrna_info,
        "COVID-19 protein subunit Vaccine" to R.string.covid_protein_info,
        "BCG - Tuberculosis" to R.string.bcg_info,
        "Hepatitis B Vaccine" to R.string.hepatis_b_info,
        "Poliovirus Vaccine" to R.string.polio_info,
        "DTP - Diphtheria Vaccine" to R.string.dtp_diphtheria_info,
        "DTP - Tetanus Vaccine" to R.string.dtp_tetanus_info,
        "DTP - Pertussis Vaccine" to R.string.dtp_pertussis_info,
        "Hib - Haemophilus influenza type b" to R.string.hib_info,
        "Pneumococcal Vaccine" to R.string.pneumococcal_info,
        "Rotavirus Vaccine" to R.string.rotavirus_info,
        "MMR - Measles Vaccine" to R.string.mmr_measles_info,
        "MMR - Mumps Vaccine" to R.string.mmr_mumps_info,
        "MMR - Rubella Vaccine" to R.string.mmr_rubella_info,
        "HPV Vaccine" to R.string.hpv_info
        )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        btnBackHome = findViewById(R.id.btnBackHome)

        btnBackHome.setOnClickListener {
            startActivity(
                Intent(this@InfoActivity,
                HomeActivity::class.java)
            )
        }

        listView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vaccineList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedVaccine = vaccineList[position]
            showVaccineInfoDialog(selectedVaccine)
        }
    }

    private fun showVaccineInfoDialog(vaccineName: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_vaccine_info)

        val tvVaccineName = dialog.findViewById<TextView>(R.id.tvVaccineName)
        val tvVaccineInfo = dialog.findViewById<TextView>(R.id.tvVaccineInfo)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        tvVaccineName.text = vaccineName

        val vaccineInfoResId = vaccineInfoMap[vaccineName] ?: R.string.default_vaccine_info
        tvVaccineInfo.text = getString(vaccineInfoResId)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
