package com.liminfinite.samparkApp.PSMSampark.karyakarAssignments;

import com.liminfinite.samparkApp.PSMSampark.dao.*;
import com.liminfinite.samparkApp.PSMSampark.distanceMatrix.DistanceMatrixUtil;
import com.liminfinite.samparkApp.PSMSampark.models.entities.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public class AssignmentBuilder {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private KaryakarRepository karyakarRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DistanceMatrixRepository distanceMatrixRepository;

    @Autowired
    private SamparkAssignmentRepository samparkAssignmentRepository;

    @Autowired
    private EntityManager entityManager;

    Logger logger = LoggerFactory.getLogger(AssignmentBuilder.class);

    public void buildSamparkAssignments(){

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

                    //Get List of Contacts from zone that are not assigned
                    List<Contact> contacts = getContacts(aZone);

                    //Get List of Distance Matrices
                    List<Long> contactIds = contacts.stream().map(c -> c.getPersonId()).collect(Collectors.toList());
                    TypedQuery<DistanceMatrix> query = entityManager.createQuery(
                            "SELECT d FROM DistanceMatrix d " +
                                    "WHERE d.karyakarId = ?1  AND d.contactId IN (?2)" +
                                    "ORDER BY d.distance ASC"
                            , DistanceMatrix.class);
                    query.setParameter(1, karyakar.getPersonId());
                    query.setParameter(2, contactIds);
                    int limit = 25 - karyakar.getAssignments();
                    query.setMaxResults(limit);
                    List<DistanceMatrix> distaceMetrices = query.getResultList();

                    //Set Distance Metrics to be SamparkAssignment
                    List<SamparkAssignment> samparkAssignments = null;

                    if(null != distaceMetrices) {
                        samparkAssignments = new ArrayList<>();

                        for (DistanceMatrix distanceMatrix : distaceMetrices) {
                            SamparkAssignment samparkAssignment = new SamparkAssignment(distanceMatrix.getKaryakarId(),
                                    distanceMatrix.getContactId(), aZone.getName());

                            samparkAssignments.add(samparkAssignment);
                        }

                        samparkAssignmentRepository.saveAll(samparkAssignments);
                    }

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
        TypedQuery<Contact> query = entityManager.createQuery(
                "SELECT c FROM Contact c " +
                        "WHERE c.personId NOT IN (select sa.contactId from SamparkAssignment sa where zone = ?1) " +
                        "AND c.zone = ?2"
                        , Contact.class);
        query.setParameter(1, zone.getName());
        query.setParameter(2, zone.getName());
        List<Contact> contactList = query.getResultList();

        return contactList;
    }
}
