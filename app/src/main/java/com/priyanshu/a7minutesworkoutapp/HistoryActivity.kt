package com.priyanshu.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanshu.a7minutesworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var bindingHistory: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHistory = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(bindingHistory?.root)

        setSupportActionBar(bindingHistory?.toolBarHistory)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        bindingHistory?.toolBarHistory?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        getAllCompletedDates(historyDao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao){
        Log.e("Date: ", "Get all completed dates run...")
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect{
                if(it.isNotEmpty()){
                    val historyAdapter = HistoryAdapter(it)
                    bindingHistory?.rvHistoryItem?.layoutManager = LinearLayoutManager(applicationContext)
                    bindingHistory?.rvHistoryItem?.adapter = historyAdapter
                    bindingHistory?.rvHistoryItem?.visibility = VISIBLE
                    bindingHistory?.tvNoRecords?.visibility = GONE
                }
                else{
                    bindingHistory?.rvHistoryItem?.visibility = GONE
                    bindingHistory?.tvNoRecords?.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingHistory = null
    }
}