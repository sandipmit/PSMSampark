package com.liminfinite.samparkApp.PSMSampark.distanceMatrix;

import com.liminfinite.samparkApp.PSMSampark.dao.ContactRepository;
import com.liminfinite.samparkApp.PSMSampark.dao.DistanceMatrixRepository;
import com.liminfinite.samparkApp.PSMSampark.dao.KaryakarRepository;
import com.liminfinite.samparkApp.PSMSampark.dao.ZoneRepository;
import com.liminfinite.samparkApp.PSMSampark.geocoding.GeoCodeUtil;
import com.liminfinite.samparkApp.PSMSampark.models.LatLong;
import com.liminfinite.samparkApp.PSMSampark.models.entities.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public class DistanceMatrixBuilder {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private KaryakarRepository karyakarRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DistanceMatrixUtil distanceMatrixUtil;

    @Autowired
    private DistanceMatrixRepository distanceMatrixRepository;

    Logger logger = LoggerFactory.getLogger(DistanceMatrixBuilder.class);

    public void buildDistanceMatrix(){

        String zoneName = "JFK";
        Zone zone = new Zone();
        zone.setName(zoneName);
        zone.setIsActive(true);
        Example<Zone> zoneExample = Example.of(zone);
        List<Zone> zones = zoneRepository.findAll(zoneExample);
        logger.info("Zone List : " + zones);

        if(null != zones) {
            for (Zone aZone: zones){
                List<Karyakar> karyakars = getKaryakars(zone);
                for (Karyakar karyakar : karyakars) {
                    logger.info("Karyakar " + karyakar);

                    List<Contact> contacts = getContacts(zone);

                    List<GeoCode> contactGeoCodes = new ArrayList<>();
                    for (Contact c : contacts) {
                        contactGeoCodes.add(c.getGeoCode());
                    }

                    List<DistanceMatrix> distanceMatrices = distanceMatrixUtil.calcualteDistaceMatrix(karyakar.getGeoCode(), contactGeoCodes);
                    logger.info("Distance Matrix : " + distanceMatrices);

                    distanceMatrixRepository.saveAll(distanceMatrices);

                    //TODO Remove
                    //break;
                }
            }
        }
    }

    public List<Karyakar> getKaryakars(Zone zone) {
        Karyakar karyakar = new Karyakar();
        karyakar.setZone(zone.getName());
        karyakar.setIsActive("Yes");
        Example<Karyakar> karyakarExample = Example.of(karyakar);
        List<Karyakar> karyakarList = karyakarRepository.findAll(karyakarExample);
        logger.debug("Karyakar List : " + karyakarList);

        return karyakarList;
    }

    public List<Contact> getContacts(Zone zone) {
        Contact person = new Contact();
        person.setZone(zone.getName());
        Example<Contact> personExample = Example.of(person);
        List<Contact> personList = contactRepository.findAll(personExample);

        logger.debug("Person List : " + personList);

        //TODO Remove
//        List<Contact> shortList = new ArrayList<>();
//        for(int i = 0; i < 1; i++){
//            shortList.add(personList.get(i));
//        }
//        logger.info("Short Person List : " + shortList);
//        personList = shortList;

        return personList;
    }
}
