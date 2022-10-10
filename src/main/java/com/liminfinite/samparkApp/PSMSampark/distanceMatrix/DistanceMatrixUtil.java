package com.liminfinite.samparkApp.PSMSampark.distanceMatrix;

import com.amazonaws.services.location.AmazonLocation;
import com.amazonaws.services.location.AmazonLocationClientBuilder;
import com.amazonaws.services.location.model.CalculateRouteMatrixRequest;
import com.amazonaws.services.location.model.CalculateRouteMatrixResult;
import com.amazonaws.services.location.model.DistanceUnit;
import com.amazonaws.services.location.model.RouteMatrixEntry;
import com.liminfinite.samparkApp.PSMSampark.geocoding.GeoCodeUtil;
import com.liminfinite.samparkApp.PSMSampark.models.entities.DistanceMatrix;
import com.liminfinite.samparkApp.PSMSampark.models.entities.GeoCode;
import com.liminfinite.samparkApp.PSMSampark.models.entities.Karyakar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;

@Component
public class DistanceMatrixUtil {
    Logger logger = LoggerFactory.getLogger(DistanceMatrixUtil.class);

    public List<DistanceMatrix> calcualteDistaceMatrix(GeoCode karyakarGeoCode, List<GeoCode> contactGeoCodes){
        List<DistanceMatrix> distanceMatrices = null;

        //Set date as Next Sunday 10:30 am
//        final LocalDate nextSunday = LocalDate.now().with( next( SUNDAY ) );
//        nextSunday.atTime(10, 30);
//        Date departureTime = Date.from(Instant.from(nextSunday));
//        logger.info("Departure time, next Sunday 10:30 am" + departureTime);

        AmazonLocation amazonLocation = AmazonLocationClientBuilder.defaultClient();
        CalculateRouteMatrixResult routeMatrixResult = amazonLocation.calculateRouteMatrix(new CalculateRouteMatrixRequest().withCalculatorName("EdisonPlacesRouteCalculator")
                .withDeparturePositions(buildDeparturePositions(karyakarGeoCode))
                .withDestinationPositions(buildDestinationPositions(contactGeoCodes))
                .withDistanceUnit(DistanceUnit.Miles)
                //.withDepartureTime(departureTime)
        );

        if(null != routeMatrixResult && null != routeMatrixResult.getRouteMatrix()) {
            List<List<RouteMatrixEntry>> routeMatrix = routeMatrixResult.getRouteMatrix();
            distanceMatrices = convertToDistanceMatrix(karyakarGeoCode, contactGeoCodes, routeMatrix);
        }

        return distanceMatrices;
    }

    private List<List<Double>> buildDeparturePositions(GeoCode karyakarGeoCode){
        List<List<Double>> latlongsList = null;

        if(null != karyakarGeoCode){
            latlongsList = new ArrayList<>();
            List<Double> latLongs = buildLatLongList(karyakarGeoCode);
            latlongsList.add(latLongs);
        }

        return latlongsList;
    }

    private List<List<Double>> buildDestinationPositions(List<GeoCode> contactGeoCodes){
        List<List<Double>> latlongsList = null;

        if(null != contactGeoCodes){
            latlongsList = new ArrayList<>();
            for (GeoCode geoCode: contactGeoCodes) {
                List<Double> latLongs = buildLatLongList(geoCode);
                latlongsList.add(latLongs);
            }
        }

        return latlongsList;
    }

    private List<Double> buildLatLongList(GeoCode geoCode){
        List<Double> latLongs = null;

        if(null != geoCode){
            latLongs = new ArrayList<>();
            latLongs.add(geoCode.getLongitude());
            latLongs.add(geoCode.getLatitude());
        }

        return latLongs;
    }

    private List<DistanceMatrix> convertToDistanceMatrix(GeoCode karyakarGeoCode, List<GeoCode> contactGeoCodes, List<List<RouteMatrixEntry>> routeMatrix){
          List<DistanceMatrix> distanceMatrices = new ArrayList<>();

          if(null != routeMatrix){
              for (List<RouteMatrixEntry> routeMatrixEntryList: routeMatrix) {
                  int i = 0;
                  for (RouteMatrixEntry routeMatrixEntry: routeMatrixEntryList) {
                      Double distance = routeMatrixEntry.getDistance();
                      Double durationSeconds = routeMatrixEntry.getDurationSeconds();

                      DistanceMatrix distanceMatrix = new DistanceMatrix(karyakarGeoCode.getPersonId(),
                              contactGeoCodes.get(i++).getPersonId(),
                              distance, durationSeconds);
                      distanceMatrices.add(distanceMatrix);
                  }
              }
          }

          return distanceMatrices;
    }
}
