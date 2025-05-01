package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.in5600_project.R
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage


@Composable
fun MapBox(
    initialLatitude: Double?,
    initialLongitude: Double?,
    interactive: Boolean = true,
    onLocationChanged: (Double, Double) -> Unit = { _, _ -> }
) {

    // Track the marker position internally for immediate UI feedback
    var markerLatitude by remember { mutableStateOf(initialLatitude) }
    var markerLongitude by remember { mutableStateOf(initialLongitude) }

    // Set up the map state (camera position, zoom, etc.)
    // If we have an initial lat/long, we use it, otherwise pick some default center
    val mapState = rememberMapViewportState {
        setCameraOptions {
            zoom(5.0)
            if (markerLatitude != null && markerLongitude != null) {
                center(Point.fromLngLat(markerLongitude!!, markerLatitude!!))
            } else {
                // Set default to Oslo
                center(Point.fromLngLat(10.7522, 59.9139))
            }
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Icon for the marker
    val markerIcon = rememberIconImage(
        key = R.drawable.red_marker, painter = painterResource(R.drawable.red_marker)
    )

    // Map click listener
    val onMapClickListener = if (interactive) {
        { point: Point ->

            // Place the marker at the tapped location
            markerLatitude = point.latitude()
            markerLongitude = point.longitude()

            // Update the map view to show the marker
            onLocationChanged(markerLatitude!!, markerLongitude!!)

            true
        }
    } else {
        null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

        // Show the map
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapState,
            onMapClickListener = onMapClickListener
        ) {
            // Show the marker only if we have valid coordinates
            if (markerLatitude != null && markerLongitude != null) {
                PointAnnotation(
                    point = Point.fromLngLat(markerLongitude!!, markerLatitude!!)
                ) {
                    iconImage = markerIcon
                }
            }
        }
    }
}
