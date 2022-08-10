package ru.kryz.max.RestServer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kryz.max.RestServer.models.Measurement;
import ru.kryz.max.RestServer.services.SensorService;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    //show spring which entity need to validate
    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz); //supports only measurement validation
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement =  (Measurement) target;

        //no sensor
        if(measurement.getSensor() == null){
            return;
        }
        //some sensor exists, then check was it registered?
        if (sensorService.findByName(measurement.getSensor().getName()).isEmpty()) //unregister sensor
            errors.rejectValue("sensor", "Sensor with such name doesn't exist");
    }
}
