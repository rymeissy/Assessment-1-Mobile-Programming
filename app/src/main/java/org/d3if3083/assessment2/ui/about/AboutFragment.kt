package org.d3if3083.assessment2.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Menyembunyikan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE
    }
}

