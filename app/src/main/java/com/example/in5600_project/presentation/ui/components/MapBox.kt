package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.in5600_project.R
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage

@Composable
fun MapBox(
    latitude: Double,
    longitude: Double,
    interactive: Boolean = true,
    onLocationChanged: (Double, Double) -> Unit = { _, _ -> }
) {
    // Store the marker position locally for immediate UI feedback
    var markerLatitude by remember { mutableDoubleStateOf(latitude) }
    var markerLongitude by remember { mutableDoubleStateOf(longitude) }

    // Set up the map state (camera position, zoom, etc.)
    val mapState = rememberMapViewportState {
        setCameraOptions {
            zoom(5.0)
            center(Point.fromLngLat(markerLongitude, markerLatitude))
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Icon for the marker
    val markerIcon = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource(R.drawable.red_marker)
    )

    // Provide an onMapClickListener only if interactive == true
    val onMapClickListener = if (interactive) {
        { point: Point ->
            markerLongitude = point.longitude()
            markerLatitude = point.latitude()

            // Recenter the camera on the newly selected location
            mapState.setCameraOptions {
                center(Point.fromLngLat(markerLongitude, markerLatitude))
            }

            // IMPORTANT: inform the parent that the location changed
            onLocationChanged(markerLatitude, markerLongitude)

            true
        }
    } else {
        null
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapState,
        onMapClickListener = onMapClickListener
    ) {
        // Show the marker at the current (markerLongitude, markerLatitude)
        PointAnnotation(
            point = Point.fromLngLat(markerLongitude, markerLatitude)
        ) {
            iconImage = markerIcon
        }
    }
}
