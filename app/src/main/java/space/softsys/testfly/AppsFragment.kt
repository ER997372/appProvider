package space.softsys.testfly

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import space.softsys.testfly.databinding.FragmentAppsBinding
import space.softsys.testfly.model.App
import space.softsys.testfly.ui.adapter.AppsAdapter
import space.softsys.testfly.viewmodel.AppsViewModel
import java.util.Objects

class AppsFragment : Fragment() {

    private val viewModel: AppsViewModel by viewModels()

    private lateinit var binding: FragmentAppsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppsAdapter
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_apps, container, false)
        binding = FragmentAppsBinding.bind(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupData()

    }

    private fun setupUI() {
        recyclerView = binding.appsList
        adapter = AppsAdapter(arrayListOf()) { app -> onItemClicked(app) }
        binding.swiper.setOnRefreshListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.queryApps()
        }

        recyclerView.adapter = adapter
    }

    private fun setupData() {
        viewModel.fetchApps().observe(this.viewLifecycleOwner) {
            binding.swiper.isRefreshing = false
            binding.loading.visibility = View.GONE
            Toast.makeText(requireContext(), it.mensaje, Toast.LENGTH_SHORT).show()
            if(Objects.nonNull(it) && Objects.nonNull(it.apps) && it.apps.isNotEmpty()) {
                adapter.clearData()
                adapter.fillData(it.apps)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onItemClicked(app: App) {
        val bundle = Bundle()
        bundle.putString("name", app.name)
        bundle.putString("version", app.version)
        bundle.putString("lastUpdate", app.updatedAt)

        findNavController().navigate(R.id.action_AppsFragment_to_DetailsFragment, bundle)
    }
}