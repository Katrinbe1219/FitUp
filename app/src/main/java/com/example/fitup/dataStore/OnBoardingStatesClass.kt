package com.example.fitup.dataStore

sealed class OnBoardingStatesClass {
    object Incomplete: OnBoardingStatesClass()
    object Completed: OnBoardingStatesClass()
    object NotChecked: OnBoardingStatesClass()
}