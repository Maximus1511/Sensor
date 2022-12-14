package ru.kryz.max.RestServer.services;

import org.springframework.stereotype.Service;
import ru.kryz.max.RestServer.models.Measurement;
import ru.kryz.max.RestServer.repositories.MeasurementRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    public MeasurementService(MeasurementRepository measurementRepository,
                              SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
       /* from JSON we have just String, no data in persistence context. This "just String" i
        have to find in db, and after "findByName" sensor is in persistence context*/
        measurement.setSensor(sensorService.findByName(measurement.getSensor().getName()).get());

        measurement.setMeasurementDateTime(LocalDateTime.now());
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

}
