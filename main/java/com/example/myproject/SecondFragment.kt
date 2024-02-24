package com.example.myproject

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myproject.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get user id from shared preferences
        val sharedPreferences =
            requireContext().getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "default").toString()

        // Get values passed from the previous fragment
        val receivedValue = arguments?.getString("key")
        val receivedValue2 = arguments?.getString("key2")

        // Hide buttons based on the received values
        if (receivedValue.equals(""))
            binding.buttonSecond.visibility = View.INVISIBLE
        else
            binding.buttonInsert.visibility = View.INVISIBLE

        // Store old values
        val oldCord = receivedValue
        val oldDate = receivedValue2

        // Button to update task
        binding.buttonSecond.setOnClickListener {
            val cord = binding.editviewSecond.text.toString()
            val date = binding.editviewDate.text.toString()

            // Update task in the background
            val updateTask = UpdateTodoTask(userId, cord, date, oldCord, oldDate)
            updateTask.execute()

            // Display appropriate toast messages
            if (cord.isBlank())
                Toast.makeText(context, "You didn't put in an assignment", Toast.LENGTH_SHORT)
                    .show()
            if (date.isBlank())
                Toast.makeText(context, "You didn't put in a date", Toast.LENGTH_SHORT).show()
            if (cord.isNotBlank() && date.isNotBlank())
                Toast.makeText(context, "A task has been updated", Toast.LENGTH_SHORT).show()
        }

        // Button to insert task
        binding.buttonInsert.setOnClickListener {
            val key = 4

            val cord = binding.editviewSecond.text.toString()
            val date = binding.editviewDate.text.toString()

            // Insert task in the background
            val addTodoTask = AddTodoTask(key, cord, date, userId)
            addTodoTask.execute()

            // Display appropriate toast messages
            if (cord.isBlank())
                Toast.makeText(context, "You didn't put in an assignment", Toast.LENGTH_SHORT)
                    .show()

            if (date.isBlank())
                Toast.makeText(context, "You didn't put in a date", Toast.LENGTH_SHORT).show()
            if (cord.isNotBlank() && date.isNotBlank())
                Toast.makeText(context, "A task has been added", Toast.LENGTH_SHORT).show()
        }

        // Set the received values to the edit text fields
        val editableValue = Editable.Factory.getInstance().newEditable(receivedValue)
        val editableValue2 = Editable.Factory.getInstance().newEditable(receivedValue2)
        binding.editviewSecond.text = editableValue
        binding.editviewDate.text = editableValue2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
