package com.example.attendance.service;

import static java.lang.Math.*;
import com.example.attendance.model.AttendanceData;
import com.example.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    
    private static final double INSTITUTE_LATITUDE = 12.9165171;
    private static final double INSTITUTE_LONGITUDE = 77.6014199;
    private static final double RADIUS_LIMIT = 50.0;

    public boolean isWithinRadius(double studentLatitude, double studentLongitude){
        double distance = haversine(INSTITUTE_LATITUDE, INSTITUTE_LONGITUDE, studentLatitude,studentLongitude);
        return distance <= RADIUS_LIMIT;
    }

    private double haversine(double lat1,  double lon1, double lat2, double lon2){
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = sin(latDistance/2) * sin(latDistance/2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(lonDistance/2) * sin(lonDistance/2);
        double c = 2 * Math.atan2(sqrt(a), sqrt(1 - a));
        double distance = R * c * 1000;
        return distance;
    }
    @Autowired
    private AttendanceRepository attendanceRepository;

    public void saveAttendance(AttendanceData attendanceData) {
        attendanceRepository.save(attendanceData);
    }


}
