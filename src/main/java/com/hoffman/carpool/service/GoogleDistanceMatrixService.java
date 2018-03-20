package com.hoffman.carpool.service;

import com.google.maps.model.DistanceMatrix;

public interface GoogleDistanceMatrixService {
    public DistanceMatrix estimateRouteTime(String departure, String arrival);
}
