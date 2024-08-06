package space.softsys.testfly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import space.softsys.testfly.databinding.FragmentDetailsBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnDescargar.setOnClickListener {

        }
    }

    private fun setData() {
        binding.appName.text = arguments?.getString("name")
        binding.appVersion.text = arguments?.getString("version")
        binding.lastUpdate.text = arguments?.getString("lastUpdate")

    }
}