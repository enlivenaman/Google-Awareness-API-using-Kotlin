package com.freelance.awarenessapi

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.snapshot.DetectedActivityResult
import com.google.android.gms.awareness.state.HeadphoneState
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class SnapshotApiActivity : AppCompatActivity() {
    private var mGoogleApiClient: GoogleApiClient? = null
    private val TAG = "Awareness"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mGoogleApiClient = GoogleApiClient.Builder(this@SnapshotApiActivity)
                .addApi(Awareness.API)
                .build()
        mGoogleApiClient?.connect()
        if (ContextCompat.checkSelfPermission(
                        this@SnapshotApiActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this@SnapshotApiActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1
            );
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        ContextChooseActivity.setOnClickListener { view ->
            initActivitySnapshots();
        }
        ContextChooseHeadphone.setOnClickListener { view ->
            initHeadPhoneSnapshots();
        }
        ContextChooseLocation.setOnClickListener { view ->
            initLocationSnapshots()
        }
        ContextChoosePlace.setOnClickListener { view ->
            initPlaceSnapshots();
        }
        ContextChooseWeather.setOnClickListener { view ->
            initWeatherSnapshots();
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initActivitySnapshots() {
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(object : ResultCallback<DetectedActivityResult> {
                    override fun onResult(detectedActivityResult: DetectedActivityResult) {
                        if (!detectedActivityResult.status.isSuccess) {
                            Log.e(TAG, "Could not get the current activity.")
                            return
                        }
                        val ar = detectedActivityResult.activityRecognitionResult
                        val probableActivity = ar.mostProbableActivity
                        snapshotApiData.setText(probableActivity.toString())
                        Log.i(TAG, probableActivity.toString())
                    }
                })
    }

    private fun initHeadPhoneSnapshots() {
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(ResultCallback { headphoneStateResult ->
                    if (!headphoneStateResult.status.isSuccess) {
                        Log.e(TAG, "Could not get headphone state.")
                        return@ResultCallback
                    }
                    val headphoneState = headphoneStateResult.headphoneState
                    if (headphoneState.state == HeadphoneState.PLUGGED_IN) {
                        Log.i(TAG, "Headphones are plugged in.\n")
                        snapshotApiData.setText("Headphones are plugged in.\n")
                    } else {
                        Log.i(TAG, "Headphones are NOT plugged in.\n")
                        snapshotApiData.setText("Headphones are NOT plugged in.\n")

                    }
                })
    }

    @SuppressLint("MissingPermission")
    private fun initLocationSnapshots() {
        Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                .setResultCallback(ResultCallback { locationResult ->
                    if (!locationResult.getStatus().isSuccess()) {
                        Log.e(TAG, "Could not get location.")
                        return@ResultCallback
                    }
                    val location = locationResult.getLocation()
                    Log.i(TAG, "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude())
                    snapshotApiData.setText("Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude())
                })
    }

    @SuppressLint("MissingPermission")
    private fun initPlaceSnapshots() {
        Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                .setResultCallback(ResultCallback { placesResult ->
                    if (!placesResult.status.isSuccess) {
                        Log.e(TAG, "Could not get places.")
                        return@ResultCallback
                    }
                    val placeLikelihoodList = placesResult.placeLikelihoods
                    // Show the top 5 possible location results.
                    if (placeLikelihoodList != null) {
                        var i = 0

                        val data = StringBuilder()
                        while (i < placeLikelihoodList.size) {
                            val p = placeLikelihoodList[i]
                            Log.i(TAG, p.place.name.toString() + ", likelihood: " + p.likelihood)
                            data.append(p.place.name.toString() + ", likelihood: " + p.likelihood + "\n")
                            i++
                        }
                        Log.i(TAG, "final : " + data.toString())
                        snapshotApiData.setText(data.toString())
                    } else {
                        Log.e(TAG, "Place is null.")
                    }
                })
    }

    @SuppressLint("MissingPermission")
    private fun initWeatherSnapshots() {
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                .setResultCallback(ResultCallback { weatherResult ->
                    if (!weatherResult.status.isSuccess) {
                        Log.e(TAG, "Could not get weather.")
                        return@ResultCallback
                    }
                    val weather = weatherResult.weather
                    Log.i(TAG, "Weather: $weather")
                    snapshotApiData.setText("Weather: $weather")
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1)

    }
}
