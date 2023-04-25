package com.priyanshu.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import com.priyanshu.a7minutesworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {
    private var bmiBinding: ActivityBmiBinding? = null

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bmiBinding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(bmiBinding?.root)

        setSupportActionBar(bmiBinding?.toolBarBmi)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI Calculator"
        }
        bmiBinding?.toolBarBmi?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        bmiBinding?.btnCalculateBMI?.setOnClickListener {
            calculate()
        }

        bmiBinding?.rgUnits?.setOnCheckedChangeListener{_, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits){
                makeMetricUnitsVisible()
            }
            else makeUSUnitsVisible()
        }
    }

    private fun makeMetricUnitsVisible(){
        currentVisibleView = METRIC_UNITS_VIEW
        bmiBinding?.llMetricEditText?.visibility = VISIBLE
        bmiBinding?.llUSEditText?.visibility = INVISIBLE

        bmiBinding?.etHeight?.text!!.clear()
        bmiBinding?.etWeight?.text!!.clear()

        bmiBinding?.llBmiTexts?.visibility = INVISIBLE
    }

    private fun makeUSUnitsVisible(){
        currentVisibleView = US_UNITS_VIEW
        bmiBinding?.llMetricEditText?.visibility = INVISIBLE
        bmiBinding?.llUSEditText?.visibility = VISIBLE

        bmiBinding?.etUSHeightFt?.text!!.clear()
        bmiBinding?.etUSHeightIn?.text!!.clear()
        bmiBinding?.etUSWeight?.text!!.clear()

        bmiBinding?.llBmiTexts?.visibility = INVISIBLE
    }

    private fun validateMetricValues(): Boolean{
        return !(bmiBinding?.etWeight?.text.toString().isEmpty() ||
                bmiBinding?.etHeight?.text.toString().isEmpty())
    }

    private fun validateUSValues(): Boolean{
        return !(bmiBinding?.etUSWeight?.text.toString().isEmpty() ||
                bmiBinding?.etUSHeightFt?.text.toString().isEmpty() ||
                bmiBinding?.etUSHeightIn?.text.toString().isEmpty())
    }

    private fun calculate(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricValues()){
                val height : Float = bmiBinding?.etHeight?.text.toString().toFloat()/100
                val weight : Float = bmiBinding?.etWeight?.text.toString().toFloat()
                val bmi : Float = weight/(height*height)
                displayBMI(bmi)
            }
            else {
                Toast.makeText(
                    this,
                    "Enter proper values for weight and height",
                    Toast.LENGTH_LONG).show()
            }
        }
        else{
            if(validateUSValues()){
                val heightFt : Float = bmiBinding?.etUSHeightFt?.text.toString().toFloat()
                val heightIn : Float = bmiBinding?.etUSHeightIn?.text.toString().toFloat()
                val height : Float = (heightFt*12) + heightIn
                val weight : Float = bmiBinding?.etUSWeight?.text.toString().toFloat()
                val bmi : Float = 703*(weight/(height*height))
                displayBMI(bmi)
            }
            else {
                Toast.makeText(
                    this,
                    "Enter proper values for weight and height",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayBMI(bmi: Float){
        val bmiLabel : String
        val bmiDescription : String
        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        bmiBinding?.tvBmi?.text = bmiValue
        bmiBinding?.tvBmiType?.text = bmiLabel
        bmiBinding?.tvBmiMessage?.text = bmiDescription

        bmiBinding?.llBmiTexts?.visibility = VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        bmiBinding = null
    }
}