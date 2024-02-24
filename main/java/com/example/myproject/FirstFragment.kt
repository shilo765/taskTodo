package com.example.myproject
import MyAdapter
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myproject.databinding.FragmentFirstBinding
import com.example.myproject.RetrofitClient.apiEndPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "MyPrefsFile" // Name of your SharedPreferences file
    private val KEY_STRING = "savedString"

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)

        // Check if userId is already saved, if not generate a random string and save it
        if (sharedPreferences.getString("userId", "default").equals("default")) {
            val randomString = generateRandomString(20)
            val editor = sharedPreferences.edit()
            editor.putString("userId", randomString)
            editor.apply()
        }

        // Set click listener for buttonFirst
        binding.buttonFirst.setOnClickListener {
            val bundle = Bundle()

            bundle.putString("key", "")
            bundle.putString("key2", "")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }

        // Set click listener for buttonFirstUpdate
        binding.buttonFirstUpdate.setOnClickListener {
            val bundle = Bundle()
            val yourValue = binding.textviewFirst.text.toString()
            if (!yourValue.equals("")){
            bundle.putString("key", yourValue.split("   :   ")[0])
            bundle.putString("key2", yourValue.split("   :   ")[1])

                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)}
            else
                Toast.makeText(requireContext(), "No assignment has been selected", Toast.LENGTH_SHORT).show()
        }

        // Set click listener for buttonFirstDelete
        binding.buttonFirstDelete.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
            val userId=sharedPreferences.getString("userId", "default").toString()
            val yourValue = binding.textviewFirst.text.toString()
            if(yourValue.equals(""))
                Toast.makeText(requireContext(), "No assignment has been selected", Toast.LENGTH_SHORT).show()
            else{
                val deleteTask = DeleteTodoTask(userId,yourValue.split("   :   ")[0],yourValue.split("   :   ")[1])
                deleteTask.execute()
                fetchPost()
                Toast.makeText(requireContext(), "assignment deleted", Toast.LENGTH_SHORT).show()
            }




        }



        // Call fetchPost to get and display posts
        fetchPost()
    }

    // Function to generate a random string of specified length
    private fun generateRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length).map { charset.random() }.joinToString("")
    }

    // Function to fetch and display posts using Retrofit
    private fun fetchPost() {
        val apiService = RetrofitClient.apiEndPoint
        val userId = sharedPreferences.getString("userId", "default").toString()
        val call: Call<List<Post>> = apiService.getPosts(userId)

        call.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                val errorMessage = "Failed to fetch post. Error: ${t.message}"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                println(errorMessage)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts: List<Post>? = response.body()

                    if (posts != null && posts.isNotEmpty()) {
                        val formattedText = buildString {
                            for (post in posts) {
                                append("Post ID: ${post.id}, Cord: ${post.cord}, Date: ${post.date}\n")
                            }
                        }

                        val listView: ListView = binding.list1
                        val lines = formattedText.split("\n")
                        val items = mutableListOf<ListItem>()

                        for (post in posts) {
                            items.add(ListItem("${post.cord}   :   ${post.date}"))
                        }

                        val adapter = MyAdapter(requireContext(), items)
                        listView.adapter = adapter
                        listView.setOnItemClickListener { _, clickedView, position, _ ->
                            val clickedItem: ListItem = items[position]
                            binding.textviewFirst.text = clickedItem.text
                            binding.textviewFirst.visibility = View.INVISIBLE
                            for (i in 0 until listView.childCount) {
                                val listItem: View = listView.getChildAt(i)
                                listItem.setBackgroundColor(Color.TRANSPARENT)
                            }
                            clickedView.setBackgroundColor(Color.LTGRAY)
                        }
                    }
                } else {
                    // Handle error
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
