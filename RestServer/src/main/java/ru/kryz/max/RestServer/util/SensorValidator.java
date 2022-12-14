package ru.kryz.max.RestServer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kryz.max.RestServer.models.Sensor;
import ru.kryz.max.RestServer.services.SensorService;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    //show spring which entity need to validate
    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz); //supports only sensor validation
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if (sensorService.findByName(sensor.getName()).isPresent())//name found, means already exists
            errors.rejectValue("name", "Sensor already exists");//add some text to errors
    }
}
