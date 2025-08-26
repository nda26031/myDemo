package com.example.testeverything.singleselection

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testeverything.R
import com.example.testeverything.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    private val currentItems = listOf(
        SelectableItem(1, "Option 1", "First option description"),
        SelectableItem(2, "Option 2", "Second option description"),
        SelectableItem(3, "Option 3", "Third option description"),
        SelectableItem(4, "Option 4", "Fourth option description"),
        SelectableItem(5, "Option 5", "Fifth option description")
    )

    private val adapter: SelectionAdapter by lazy{
        SelectionAdapter(onItemClick = { item,position ->
            onItemClick( item,position)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter.submitList(currentItems)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onItemClick(selectedItem: SelectableItem, position: Int) {

        // Create new list with updated selection states
        val updatedList = currentItems.map { item ->
            item.copy(isSelected = item.id == selectedItem.id)
            //Dòng này kiểm tra: nếu item.id == selectedItem.id ⇒ thì gán isSelected = true, ngược lại false
        }
//        Log.d("SecondActivity","position: $position")
//        Log.d("SecondActivity", "onItemClick: $selectedItem")
        // Submit the updated list to adapter
        adapter.submitList(updatedList)
        // Optional: Handle selection callback
    }

}

