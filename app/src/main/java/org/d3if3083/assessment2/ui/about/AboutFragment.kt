package org.d3if3083.assessment2.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // menghilangkan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}

