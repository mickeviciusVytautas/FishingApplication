package com.visma.fishing.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.visma.fishing.auxilary.LocalDateDeserializer;
import com.visma.fishing.auxilary.LocalDateSerializer;
import com.visma.fishing.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@NamedQueries(
        @NamedQuery(name = "departure.findAll", query = "SELECT d FROM Departure d")
)
public class Departure extends BaseEntity {

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;
    @NotNull
    private String port;

    public Departure(Date date, String port) {
        this.date = date;
        this.port = port;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("port", port)
                .add("date", date.toString())
                .build();
    }

}
