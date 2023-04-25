package com.priyanshu.a7minutesworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanshu.a7minutesworkoutapp.databinding.ActivityExerciseBinding
import com.priyanshu.a7minutesworkoutapp.databinding.CustomDialogBackPressedBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var exerciseStatusAdapter: ExerciseStatusAdapter? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress: Int = 0
    private var restTimerDuration: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0
    private var exerciseTimerDuration: Long = 30

    private var exerciseList: ArrayList<ExerciseModule>? = null
    private var currentExercise = -1

    private var tts: TextToSpeech? = null

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarExercise?.setNavigationOnClickListener {
            setupCustomBackDialog()
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this@ExerciseActivity)

        setUpRestTimer()
        setupExerciseAdapter()
    }

    private fun setupExerciseAdapter(){
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseNumber?.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvExerciseNumber?.adapter = exerciseStatusAdapter
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_NOT_SUPPORTED ||
                result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS", "Language not supported")
            }
        }else{
            Log.e("TTS", "Initialisation Failed")
        }
    }

    private fun toSpeak(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    private fun setUpRestTimer(){
        try {
            val soundURI = Uri.parse(
                "android.resource://com.priyanshu.a7minutesworkoutapp/"
                        + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e : Exception){
            e.printStackTrace()
        }

        binding?.flRestProgressBar?.visibility = View.VISIBLE
        binding?.tvRest?.visibility = View.VISIBLE
        binding?.tvUpcomingName?.visibility = View.VISIBLE
        binding?.tvUpcoming?.visibility = View.VISIBLE
        binding?.flExerciseProgressBar?.visibility = View.INVISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE

        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingName?.text = exerciseList!![currentExercise+1].getName()

        startRestTimer()
    }

    private fun startRestTimer(){
        binding?.restProgressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                if(millisUntilFinished/1000 in 1..3)
                    toSpeak((millisUntilFinished/1000).toString())
                binding?.tvRestTimer?.text = (10 - restProgress).toString()
                binding?.restProgressBar?.progress = 10 - restProgress
            }

            override fun onFinish() {
                currentExercise++
                exerciseList!![currentExercise].setIsSelected(true)
                exerciseStatusAdapter?.notifyDataSetChanged()
                setUpExerciseTimer()
            }
        }.start()
    }

    private fun setUpExerciseTimer(){
        binding?.flRestProgressBar?.visibility = View.INVISIBLE
        binding?.tvRest?.visibility = View.INVISIBLE
        binding?.tvUpcoming?.visibility = View.INVISIBLE
        binding?.tvUpcomingName?.visibility = View.INVISIBLE
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        binding?.ivExercise?.setImageResource(exerciseList!![currentExercise].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercise].getName()

        toSpeak(exerciseList!![currentExercise].getName())
        toSpeak("30 Seconds")
        startExerciseTimer()
    }

    private fun startExerciseTimer(){
        binding?.exerciseProgressBar?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                if(millisUntilFinished/1000 in 1..3)
                    toSpeak((millisUntilFinished/1000).toString())
                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()
                binding?.exerciseProgressBar?.progress = 30 - exerciseProgress
            }

            override fun onFinish() {
                if(exerciseList!!.size -1 > currentExercise){
                    toSpeak("Take Rest")
                    toSpeak(binding?.tvRest?.text.toString())
                    toSpeak("Next Exercise")
                    toSpeak(exerciseList!![currentExercise+1].getName())

                    exerciseList!![currentExercise].setIsSelected(false)
                    exerciseList!![currentExercise].setIsCompleted(true)
                    exerciseStatusAdapter?.notifyDataSetChanged()
                    setUpRestTimer()
                }
                else{
                    tts?.stop()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    private fun setupCustomBackDialog(){
        val dialogBinding = CustomDialogBackPressedBinding.inflate(layoutInflater)
        val customDialog = Dialog(this)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        setupCustomBackDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
            player!!.reset()
            player!!.release()
        }

        binding = null
    }
}