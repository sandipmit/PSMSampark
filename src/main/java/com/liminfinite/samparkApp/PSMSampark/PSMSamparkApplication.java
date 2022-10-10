package com.liminfinite.samparkApp.PSMSampark;

import com.liminfinite.samparkApp.PSMSampark.distanceMatrix.DistanceMatrixBuilder;
import com.liminfinite.samparkApp.PSMSampark.geocoding.GeoCoder;
import com.liminfinite.samparkApp.PSMSampark.karyakarAssignments.AssignmentBuilder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Data
@ComponentScan("com.liminfinite.samparkApp.PSMSampark")
@EnableJpaRepositories(basePackages = "com.liminfinite.samparkApp.PSMSampark.dao")
@EntityScan("com.liminfinite.samparkApp.PSMSampark.models")
public class PSMSamparkApplication implements CommandLineRunner {

    @Autowired
    private DistanceMatrixBuilder distanceMatrixBuilder;

    @Autowired
    private GeoCoder geoCoder;

    @Autowired
    private AssignmentBuilder assignmentBuilder;

    Logger logger = LoggerFactory.getLogger(PSMSamparkApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PSMSamparkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String command = "";

        if(args.length == 1){
            if("geocoding".equals(args[0])){
                geoCoder.geoCode();
            }else if("distance-matrix".equals(args[0])){
                distanceMatrixBuilder.buildDistanceMatrix();
            }else if("sampark-assignment".equals(args[0])){
                assignmentBuilder.buildSamparkAssignments();
            }
        }else{
            logger.error("Invalid input parameter");
        }
    }
}