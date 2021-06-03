package com.example.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(val context: Context, val lifecycle: Lifecycle) : LifecycleObserver {
    private val tag = this::class.simpleName
    private var currentLat = 0.0
    private var currentLon = 0.0

    private val locManager = PseudoLocationManager(context) { lat, lon ->
        currentLat = lat
        currentLon = lon
        Log.d(tag, "Location Callback Lat:$currentLat Lon:$currentLon")
    }

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler(){
        locManager.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler(){
        locManager.stop()
    }
}