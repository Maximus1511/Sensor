package ru.kryz.max.RestServer.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kryz.max.RestServer.dto.SensorDTO;
import ru.kryz.max.RestServer.models.Sensor;
import ru.kryz.max.RestServer.services.SensorService;
import ru.kryz.max.RestServer.util.MeasurementErrorResponse;
import ru.kryz.max.RestServer.util.MeasurementException;
import ru.kryz.max.RestServer.util.SensorValidator;

import javax.validation.Valid;
import java.util.List;

import static ru.kryz.max.RestServer.util.ErrorsUtil.returnErrorsToClient;

@RestController //methods don't return views, return data
@RequestMapping("/sensors")
public class SensorsController {

    //inject some fields
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorsController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    //if want to success sensor
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult){
        Sensor sensorForAdd =  convertToSensor(sensorDTO);//convert from DTO to Sensor object
        sensorValidator.validate(sensorForAdd, bindingResult);//validate converted object

        if(bindingResult.hasErrors()){ //some errors, some invalid request
            returnErrorsToClient(bindingResult);
        }
        sensorService.register(sensorForAdd);// everything is ok, register new sensor
        return ResponseEntity.ok(HttpStatus.OK);//http response with empty body and status 200
    }

    //converter DTO->object
    private Sensor convertToSensor(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    @ExceptionHandler //catch Measurement Exception, need to send to client "bad request" response
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response =
                new MeasurementErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/registration")
    public List<Sensor> getSensors(){
        return sensorService.findAll();
    }
}
