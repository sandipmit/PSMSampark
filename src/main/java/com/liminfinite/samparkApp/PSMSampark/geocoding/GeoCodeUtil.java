package com.liminfinite.samparkApp.PSMSampark.geocoding;

import com.amazonaws.services.location.AmazonLocation;
import com.amazonaws.services.location.AmazonLocationClientBuilder;
import com.amazonaws.services.location.model.SearchForTextResult;
import com.amazonaws.services.location.model.SearchPlaceIndexForTextRequest;
import com.amazonaws.services.location.model.SearchPlaceIndexForTextResult;
import com.liminfinite.samparkApp.PSMSampark.models.LatLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeoCodeUtil {

    Logger logger = LoggerFactory.getLogger(GeoCodeUtil.class);

    LatLong geoCode(String address){
        LatLong geoCode = null;

        logger.debug("Geocoding....");

        AmazonLocation amazonLocation = AmazonLocationClientBuilder.defaultClient();
        SearchPlaceIndexForTextResult searchPlaceIndexForTextResult = amazonLocation.searchPlaceIndexForText(new SearchPlaceIndexForTextRequest()
                .withIndexName("EdisonPlaceIndex")
                .withText(address)
                .withMaxResults(1));
        if(null != searchPlaceIndexForTextResult) {
            logger.debug(searchPlaceIndexForTextResult.toString());
            List<SearchForTextResult> results = searchPlaceIndexForTextResult.getResults();
            if (null != results) {
                SearchForTextResult searchForTextResult = results.get(0);
                List<Double> point = searchForTextResult.getPlace().getGeometry().getPoint();
                if (null != point) {
                    Double longitude = point.get(0);
                    Double latitude = point.get(1);
                    logger.info("Lat : " + latitude + " Long : " + longitude);

                    geoCode = new LatLong(latitude, longitude);
                }
            } else {
                logger.info("Null result");
            }
        }

        return geoCode;
    }
}
