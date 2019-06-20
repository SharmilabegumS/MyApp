package com.example.navigationcheck

interface DrawableClickListener {

    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    fun onClick(target: DrawablePosition)
}