package com.reversecoder.googlemap.demo.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * @author Md. Rashadul Alam
 */

public class MapUtil {

    public static double calculateDistance(Location firstLocation, Location secondLocation) {
        double distance = 0.0;
        try {
            //For first location
            Location firstLoc = new Location("firstLocation");
            firstLoc.setLatitude(firstLocation.getLatitude());
            firstLoc.setLongitude(firstLocation.getLongitude());
            //For second location
            Location secondLoc = new Location("secondLocation");
            secondLoc.setLatitude(secondLocation.getLatitude());
            secondLoc.setLongitude(secondLocation.getLongitude());
            //Calculate distance
            distance = firstLoc.distanceTo(secondLoc);
        } catch (Exception e) {
            return 0.0;
        }
        return distance;
    }

    public static double calculateDistance(LatLng firstLocation, LatLng secondLocation) {
        double distance = 0.0;
        try {
            //For first location
            Location firstLoc = new Location("firstLocation");
            firstLoc.setLatitude(firstLocation.latitude);
            firstLoc.setLongitude(firstLocation.longitude);
            //For second location
            Location secondLoc = new Location("secondLocation");
            secondLoc.setLatitude(secondLocation.latitude);
            secondLoc.setLongitude(secondLocation.longitude);
            //Calculate distance
            distance = firstLoc.distanceTo(secondLoc);
        } catch (Exception e) {
            return 0.0;
        }
        return distance;
    }

    public static double formatDoubleValue(double number, int numberAfterDecimal) {
        String format = "##.";
        if (number > 0) {
            for (int i = 0; i < numberAfterDecimal; i++) {
                format = format + "#";
            }
        }
        double value = Double.parseDouble(new DecimalFormat(format).format(number));
        return value;
    }
}
