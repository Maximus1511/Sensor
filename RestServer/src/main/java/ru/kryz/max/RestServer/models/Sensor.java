package ru.kryz.max.RestServer.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "Sensor")
public class Sensor implements Serializable {
    //need such implementation of Serializible
    // because name (type String) is foreign key. Not Id (Integer) like it usually was.
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Input some name")
    @Column(name = "name")
    private String name;

    public Sensor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
