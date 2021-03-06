package com.allexandresantos.custombutton


sealed class ButtonState {
    object InitialState : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}