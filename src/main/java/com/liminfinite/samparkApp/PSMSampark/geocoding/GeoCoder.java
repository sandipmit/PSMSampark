package com.liminfinite.samparkApp.PSMSampark.geocoding;

import com.liminfinite.samparkApp.PSMSampark.dao.KaryakarRepository;
import com.liminfinite.samparkApp.PSMSampark.dao.ContactRepository;
import com.liminfinite.samparkApp.PSMSampark.dao.ZoneRepository;
import com.liminfinite.samparkApp.PSMSampark.models.LatLong;
import com.liminfinite.samparkApp.PSMSampark.models.entities.GeoCode;
import com.liminfinite.samparkApp.PSMSampark.models.entities.Karyakar;
import com.liminfinite.samparkApp.PSMSampark.models.entities.Contact;
import com.liminfinite.samparkApp.PSMSampark.models.entities.Zone;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class GeoCoder {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private KaryakarRepository karyakarRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private GeoCodeUtil geoCodeUtil;

    Logger logger = LoggerFactory.getLogger(GeoCoder.class);

    public void geoCode(){

        String zoneName = "JFK";
        Zone zone = new Zone();
        zone.setName(zoneName);
        zone.setIsActive(true);
        Example<Zone> zoneExample = Example.of(zone);
        List<Zone> zones = zoneRepository.findAll(zoneExample);
        logger.info("Zone List : " + zones);

        if(null != zones) {
            for (Zone aZone: zones){
                geocodeKaryakars(zone);

                geoCodeContacts(zone);
            }
        }
    }

    private void geocodeKaryakars(Zone zone){
        Karyakar karyakar = new Karyakar();
        karyakar.setZone(zone.getName());
        karyakar.setIsActive("Yes");
        Example<Karyakar> karyakarExample = Example.of(karyakar);
        List<Karyakar> karyakarList = karyakarRepository.findAll(karyakarExample);
        logger.debug("Karyakar List : " + karyakarList);

        if(null != karyakarList){
            for (Karyakar aKaryakar: karyakarList) {
                String fullAddress = aKaryakar.getAddressLine() + " " + aKaryakar.getCity() + " " + aKaryakar.getState() + " " + aKaryakar.getZipCode();

                logger.info(aKaryakar.getPersonId() + " - " + aKaryakar.getFirstName() + " " + aKaryakar.getLastName() + " : " + fullAddress);

                if(null != aKaryakar.getGeoCode()
                        && null != aKaryakar.getGeoCode().getLatitude()
                        && null != aKaryakar.getGeoCode().getLongitude()){
                    logger.info("Skipping geocoding as its already geocoded");
                }else {
                    LatLong latLong = geoCodeUtil.geoCode(fullAddress);

                    if (null != latLong) {
                        GeoCode geoCode = new GeoCode(aKaryakar.getPersonId(), latLong);
                        aKaryakar.setGeoCode(geoCode);
                    }
                }

                //TODO Remove
                //break;
            }

            List<Karyakar> savedKaryakarList = karyakarRepository.saveAll(karyakarList);
            //logger.debug("Saved Person List : " + savedPersonList);
        }

    }

    private void geoCodeContacts(Zone zone){
            Contact person = new Contact();
            person.setZone(zone.getName());
            Example<Contact> personExample = Example.of(person);
            List<Contact> personList = contactRepository.findAll(personExample);

            logger.debug("Person List : " + personList);

            if (null != personList) {

                for (Contact aPerson : personList) {
                    String fullAddress = aPerson.getAddressLine() + " " + aPerson.getCity() + " " + aPerson.getState() + " " + aPerson.getZipCode();

                    logger.info(aPerson.getPersonId() + " - " + aPerson.getFirstName() + " " + aPerson.getLastName() + " : " + fullAddress);

                    if(null != aPerson.getGeoCode()
                            && null != aPerson.getGeoCode().getLatitude()
                            && null != aPerson.getGeoCode().getLongitude()){
                        logger.info("Skipping geocoding as its already geocoded");

                    }else {
                        LatLong latLong = geoCodeUtil.geoCode(fullAddress);

                        if (null != latLong) {
                            GeoCode geoCode = new GeoCode(aPerson.getPersonId(), latLong);
                            aPerson.setGeoCode(geoCode);
                        }

                    }

                    //TODO Remove
                    //break;
                }

                List<Contact> savedPersonList = contactRepository.saveAll(personList);
                //logger.debug("Saved Person List : " + savedPersonList);
            }
    }

}
