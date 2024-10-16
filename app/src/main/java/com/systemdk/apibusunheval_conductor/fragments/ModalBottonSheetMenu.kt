package com.systemdk.apibusunheval_conductor.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.systemdk.apibusunheval_conductor.R
import com.systemdk.apibusunheval_conductor.activities.MainActivity
import com.systemdk.apibusunheval_conductor.activities.PreguntasActivity
import com.systemdk.apibusunheval_conductor.activities.ProfileActivity
import com.systemdk.apibusunheval_conductor.models.Conductor
import com.systemdk.apibusunheval_conductor.providers.AuthProvider
import com.systemdk.apibusunheval_conductor.providers.ConductorProvider

class ModalBottonSheetMenu: BottomSheetDialogFragment() {

    val conductorProvider = ConductorProvider()
    val authProvider = AuthProvider()

    var textViewUserName: TextView? = null
    var linearLayoutLogout: LinearLayout? = null
    var linearLayoutProfile: LinearLayout? = null
    var linearLayoutEdit: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_botton_sheet_menu,container, false)

        textViewUserName = view.findViewById(R.id.textViewUserName)
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout)
        linearLayoutProfile = view.findViewById(R.id.linearLayoutProfile)
        linearLayoutEdit = view.findViewById(R.id.linearLayoutEditarAsistente)


        getConductor()
        linearLayoutLogout?.setOnClickListener { goToMain() }
        linearLayoutProfile?.setOnClickListener { goToProfile() }
        linearLayoutEdit?.setOnClickListener { goToEditConsultas() }

        return view
    }

    private fun goToProfile(){
        val i = Intent(activity, ProfileActivity::class.java)
        startActivity(i)
    }

    private fun goToEditConsultas(){
        val i = Intent(activity, PreguntasActivity::class.java)
        startActivity(i)
    }

    private fun goToMain(){
        authProvider.logout()
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getConductor(){
        conductorProvider.getConductor(authProvider.getId()).addOnSuccessListener { document ->

            if (document.exists()){
                val conductor = document.toObject(Conductor::class.java)
                textViewUserName?.text = "${conductor?.names}"
            }

        }
    }

    companion object {
        const val TAG = "ModalBottonSheetMenu"

    }

}