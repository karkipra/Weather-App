package com.pratikkarki.weatherapp.touch

interface TodoTouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}