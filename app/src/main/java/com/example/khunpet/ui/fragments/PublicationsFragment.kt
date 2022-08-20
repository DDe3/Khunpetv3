package com.example.khunpet.ui.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khunpet.controllers.adapters.PublicationAdapter
import com.example.khunpet.controllers.view_models.MainActivityViewModel
import com.example.khunpet.controllers.view_models.PublicationsViewModel
import com.example.khunpet.databinding.FragmentPublicationsBinding
import com.example.khunpet.model.Publication
import com.example.khunpet.ui.activities.InfoActivity
import com.example.khunpet.ui.activities.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch


class PublicationsFragment : Fragment() {


    private val viewModel: PublicationsViewModel by viewModels()
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private var _binding: FragmentPublicationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPublicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getItems()
        }


        binding.publicarButton.setOnClickListener {
            activityViewModel.changeFragment(5)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { bool ->
            if (bool) {
                binding.publicationProgressBar.visibility = View.VISIBLE
            } else {
                binding.publicationProgressBar.visibility = View.GONE
            }
        }

        viewModel.retLiveData.observe(viewLifecycleOwner) {
            loadRecyclerView(it)
        }


    }

    private fun loadRecyclerView(items : List<Publication>) {
        binding.publicacionReciclerView.layoutManager =
            GridLayoutManager(binding.publicacionReciclerView.context, 2)
        binding.publicacionReciclerView.adapter = PublicationAdapter(items) { onClickPublication(it) }
    }

    private fun onClickPublication(publication: Publication) {
        val intent = Intent(requireActivity(),InfoActivity::class.java)
        val gson = Gson()
        val json = gson.toJson(publication)
        intent.putExtra("publication", json)
        startActivity(intent);
    }


}