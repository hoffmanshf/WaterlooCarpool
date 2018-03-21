package com.hoffman.carpool.service.ServiceImpl;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.GoogleDistanceMatrixService;
import org.springframework.stereotype.Service;

@Service
public class GoogleDistanceMatrixServiceImpl implements GoogleDistanceMatrixService {

    private static final String API_KEY = "AIzaSyACQSMkZbQvQwPw-dp-HcRI88mvxNMyaSQ";
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    @Override
    public DistanceMatrix estimateRouteTime(String departure, String arrival) {
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);

            DistanceMatrix distanceMatrix = req.origins(departure)
                    .destinations(arrival)
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .language("en-CA")
                    .await();
            return distanceMatrix;

        } catch (ApiException e) {
            throw new UServiceException("TXN_103","", "Api parse error", e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
