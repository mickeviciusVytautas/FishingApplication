package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class Arrival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    @NotNull
    private String port;
    @NotNull
    private Date date;

    public Arrival(String port, Date date) {
        this.port = port;
        this.date = date;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("port", port)
                .add("date", date.toString())
                .build();
    }
}
