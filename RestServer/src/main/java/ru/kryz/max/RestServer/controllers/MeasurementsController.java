package ru.kryz.max.RestServer.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kryz.max.RestServer.dto.MeasurementDTO;
import ru.kryz.max.RestServer.dto.MeasurementsResponse;
import ru.kryz.max.RestServer.models.Measurement;
import ru.kryz.max.RestServer.services.MeasurementService;
import ru.kryz.max.RestServer.util.MeasurementErrorResponse;
import ru.kryz.max.RestServer.util.MeasurementException;
import ru.kryz.max.RestServer.util.MeasurementValidator;

import javax.validation.Valid;

import java.util.stream.Collectors;

import static ru.kryz.max.RestServer.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/meas")
public class MeasurementsController {

    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementService measurementService,
                                  MeasurementValidator measurementValidator,
                                  ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult){
        Measurement measurementForAdd =  convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurementForAdd, bindingResult);
        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        measurementService.addMeasurement(measurementForAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/show")
    public MeasurementsResponse getMeasurements() {
        //collect list of objects to single object for transfer
        return new MeasurementsResponse(measurementService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount() {
        return measurementService.findAll().stream().filter(Measurement::isRaining).count();
    }
}
