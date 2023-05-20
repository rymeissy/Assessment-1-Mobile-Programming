package org.d3if3083.assessment2.ui.histori

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentHistoriBinding
import org.d3if3083.assessment2.db.ResepDb

class HistoriFragment : Fragment() {

    private lateinit var binding: FragmentHistoriBinding

    private val viewModel: HistoriViewModel by lazy {
        val db = ResepDb.getInstance(requireContext())
        val factory = HistoriViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HistoriViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoriBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.data.observe(viewLifecycleOwner) {
            Log.d("HistoriFragment", "Jumlah data: ${it.size}")
        }
        super.onViewCreated(view, savedInstanceState)
        // Menyembunyikan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE
    }
}