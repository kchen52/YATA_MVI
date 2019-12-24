package dev.ktown.yata_mvi.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import dev.ktown.yata_mvi.Model.MapState
import dev.ktown.yata_mvi.R
import dev.ktown.yata_mvi.TasksIntent
import io.reactivex.Observable
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity(), MainView {

    private fun intents(): Observable<TasksIntent> {
        return Observable.merge(
            initialIntent(),
            refreshIntent()
        )
    }

    private fun initialIntent(): Observable<TasksIntent> {
        return Observable.just(TasksIntent.InitialIntent)
    }

    private fun refreshIntent(): Observable<TasksIntent> {
        return Observable.just(TasksIntent.RefreshIntent)
    }

    private fun userTapIntent(): Observable<TasksIntent> {
        return Observable.just(null)
    }


    override fun render(state: MapState) {
        when (state) {
            is MapState.DataState -> drawPoints(state)
            is MapState.LoadingState -> showLoading()
            is MapState.ErrorState -> showError(state)
        }
    }

    private fun drawPoints(dataState: MapState.DataState) {
        // TODO:
        Toast.makeText(applicationContext, "data state", Toast.LENGTH_LONG).show()
    }

    private fun showError(errorState: MapState.ErrorState) {
        Toast.makeText(applicationContext, "Error: ${errorState.description}", Toast.LENGTH_LONG).show()
        Log.e("MainActivity", "An error occurred. ${errorState.description}")
    }

    private fun showLoading() {
        Toast.makeText(applicationContext, "Loading...", Toast.LENGTH_LONG).show()
    }


    private val osmMapView: MapView by lazy {
        findViewById<MapView>(R.id.osm_map)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle permissions first, before map is created. Not depicted yet lol

        // Load/initialize the osmdroid configuration.
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        // Setting this before the layout is inflated is a good idea.
        // It should ensure that the map has a writable location for the map cache, even without
        // permissions.
        // If no tiles are displayed, you can try overriding the cache path using
        // Configuration.getInstance().setCachePath
        // See also StorageUtils
        // Note: the load method also sets the HTTP User Agent to your application's package name.
        // Abusing OSM's tile servers will get you banned based on this string.
        setContentView(R.layout.activity_main)

        osmMapView.setTileSource(TileSourceFactory.MAPNIK)
        osmMapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        osmMapView.setMultiTouchControls(true)


        val mapController = osmMapView.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)

        osmMapView.setOnClickListener {
            mapController.zoomIn()
        }
    }

    override fun onResume() {
        super.onResume()
        // This will refresh the osmdroid configuration on resuming.
        // If you make changes to the configuration, use
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        osmMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        // This will refresh the osmdroid configuration on pausing.
        // If you make changes to the configuration, use
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Configuration.getInstance().save(this, prefs);
        osmMapView.onPause()
    }

}
