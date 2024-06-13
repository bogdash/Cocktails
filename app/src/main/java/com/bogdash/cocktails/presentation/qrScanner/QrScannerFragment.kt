package com.bogdash.cocktails.presentation.qrScanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.bogdash.cocktails.R
import com.bogdash.cocktails.presentation.detail.DetailFragment
import com.bogdash.cocktails.presentation.main.MainActivity
import com.bogdash.domain.models.Drink
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.serialization.json.Json

class QrScannerFragment : Fragment() {
    private lateinit var barLauncher: ActivityResultLauncher<ScanOptions>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = ScanOptions()
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(ActivityCapture::class.java)
        options.addExtra("android.intent.extras.CAMERA_FACING", 0)

        barLauncher = registerForActivityResult(
            ScanContract()
        ) { result ->
            if (result.contents != null) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, DetailFragment.newInstance(
                        DetailFragment.Input.Json(result.contents)
                    )
                ).commit()
            }
        }

        barLauncher.launch(options)
    }

}