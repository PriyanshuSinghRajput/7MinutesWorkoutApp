package com.priyanshu.a7minutesworkoutapp

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModule>{
        val exerciseList = ArrayList<ExerciseModule>()

        val jumpingJacks = ExerciseModule(1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false)
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModule(2,
            "Wallsit",
            R.drawable.ic_wallsit,
            false,
            false)
        exerciseList.add(wallSit)

        val pushUps = ExerciseModule(3,
            "Push-Ups",
            R.drawable.ic_pushups,
            false,
            false)
        exerciseList.add(pushUps)

        val crunches = ExerciseModule(4,
            "Crunches",
            R.drawable.ic_crunches,
            false,
            false)
        exerciseList.add(crunches)

        val stepUps = ExerciseModule(5,
            "Step-Ups",
            R.drawable.ic_stepups,
            false,
            false)
        exerciseList.add(stepUps)

        val squats = ExerciseModule(6,
            "Squats",
            R.drawable.ic_squats,
            false,
            false)
        exerciseList.add(squats)

        val tricepsDips = ExerciseModule(7,
            "Triceps Dips",
            R.drawable.ic_triceps_dips,
            false,
            false)
        exerciseList.add(tricepsDips)

        val plank = ExerciseModule(8,
            "Plank",
            R.drawable.ic_plank,
            false,
            false)
        exerciseList.add(plank)

        val highKnees = ExerciseModule(9,
            "High Knees",
            R.drawable.ic_high_knees,
            false,
            false)
        exerciseList.add(highKnees)

        val lunges = ExerciseModule(10,
            "Lunges",
            R.drawable.ic_lunges,
            false,
            false)
        exerciseList.add(lunges)

        val pushUpsWithRotation = ExerciseModule(11,
            "Push-Ups with rotation",
            R.drawable.ic_pushups_with_rotation,
            false,
            false)
        exerciseList.add(pushUpsWithRotation)

        val sidePlank = ExerciseModule(12,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false)
        exerciseList.add(sidePlank)

        return exerciseList
    }
}