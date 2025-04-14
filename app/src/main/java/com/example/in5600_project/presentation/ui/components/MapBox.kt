package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.in5600_project.R
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage

@Composable
fun MapBox(longitude: Double, latitude: Double) {
    var clickedLongitude by remember { mutableDoubleStateOf(longitude) }
    var clickedLatitude by remember { mutableDoubleStateOf(latitude) }

    // Set up the map state
    val mapState = rememberMapViewportState {
        setCameraOptions {
            zoom(5.0)
            center(Point.fromLngLat(longitude, latitude))
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Create  painter/icon
    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource(R.drawable.red_marker)
    )

    // Pass mapState to MapboxMap
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapState,
        onMapClickListener = { point ->
            clickedLongitude = point.longitude()
            clickedLatitude = point.latitude()
            println("Point: $point.")
            true
        }

    ) {
        println("MapBox: $clickedLongitude, $clickedLatitude")
        PointAnnotation(point = Point.fromLngLat(clickedLongitude, clickedLatitude)) {
            iconImage = marker
        }



    }
}