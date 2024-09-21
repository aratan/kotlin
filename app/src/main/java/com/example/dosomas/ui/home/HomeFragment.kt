// HomeFragment.kt

package com.example.dosomas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dosomas.databinding.FragmentHomeBinding
import org.json.JSONArray

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Volley request queue
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        // API URL
        val url = "https://sanestebanfuenlabrada.es/wp-json/wp/v2/pages"

        // Request to fetch data from API
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response: JSONArray ->
                // Parse JSON response and update UI
                displayApiData(response)
            },
            Response.ErrorListener { error ->
                // Handle error
                binding.textView.text = "Error: ${error.message}"
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest)

        return root
    }

    // Function to parse JSON and display data in TextView
    private fun displayApiData(response: JSONArray) {
        val titles = StringBuilder()
        for (i in 0 until response.length()) {
            val page = response.getJSONObject(i)
            val title = page.getJSONObject("title").getString("rendered")
            titles.append(title).append("\n")
        }
        binding.textView.text = titles.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
