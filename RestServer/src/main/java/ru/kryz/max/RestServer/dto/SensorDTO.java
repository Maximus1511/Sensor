package ru.kryz.max.RestServer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SensorDTO {

    @NotEmpty(message = "Shouldn't be empty")
    @Size(min = 3, max = 40, message = "Name should be between 3 and 40 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
