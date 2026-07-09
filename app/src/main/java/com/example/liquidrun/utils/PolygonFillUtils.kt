package com.example.liquidrun.utils

import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import com.google.android.gms.maps.model.LatLng
import kotlin.math.floor
import kotlin.math.ceil

object PolygonFillUtils {
    /**
     * Calculates the captured 0.0002 degree grid squares inside a closed path (Polygon Fill).
     * Uses Android's Path and Region by scaling float coordinates to integers.
     */
    fun calculateCapturedZones(routeCoordinates: List<LatLng>): Set<String> {
        if (routeCoordinates.size < 3) return emptySet()

        val capturedGrids = mutableSetOf<String>()
        val scale = 100000.0 // Scale coordinates to avoid float precision loss with Region
        
        val path = Path()
        val first = routeCoordinates.first()
        path.moveTo((first.longitude * scale).toFloat(), (first.latitude * scale).toFloat())
        
        for (i in 1 until routeCoordinates.size) {
            val coord = routeCoordinates[i]
            path.lineTo((coord.longitude * scale).toFloat(), (coord.latitude * scale).toFloat())
        }
        path.close()

        val bounds = RectF()
        path.computeBounds(bounds, true)
        
        val region = Region()
        region.setPath(
            path, 
            Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt())
        )

        val minLat = bounds.top / scale
        val maxLat = bounds.bottom / scale
        val minLon = bounds.left / scale
        val maxLon = bounds.right / scale

        // Step size 0.0002 degrees (approx. 22x14m)
        val latStart = floor(minLat * 5000) / 5000.0
        val latEnd = ceil(maxLat * 5000) / 5000.0
        val lonStart = floor(minLon * 5000) / 5000.0
        val lonEnd = ceil(maxLon * 5000) / 5000.0

        var currentLat = latStart
        while (currentLat <= latEnd) {
            var currentLon = lonStart
            while (currentLon <= lonEnd) {
                val pointX = (currentLon * scale).toInt()
                val pointY = (currentLat * scale).toInt()
                
                if (region.contains(pointX, pointY)) {
                    val gridLat = String.format("%.4f", currentLat).replace(",", ".")
                    val gridLon = String.format("%.4f", currentLon).replace(",", ".")
                    capturedGrids.add("${gridLat}_${gridLon}")
                }
                currentLon += 0.0002
            }
            currentLat += 0.0002
        }

        return capturedGrids
    }
}
