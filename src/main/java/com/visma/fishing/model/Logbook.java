package com.visma.fishing.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.fishing.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(name = "logbook.findAll", query = "SELECT l FROM Logbook l")
)
public class Logbook extends BaseEntity {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Departure departure;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private EndOfFishing endOfFishing;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Arrival arrival;
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Catch> catchList = new ArrayList<>();
    @NotNull
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;

    public Logbook(Departure departure, EndOfFishing endOfFishing, Arrival arrival, List<Catch> catchList, String connectionType) {
        this.departure = departure;
        this.catchList = catchList;
        this.arrival = arrival;
        this.endOfFishing = endOfFishing;
        this.communicationType = CommunicationType.valueOf(connectionType);
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String logbook = null;
        try {
            logbook = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
         return logbook;
    }

    public static class LogbookBuilder {

        private Departure departure;
        private EndOfFishing endOfFishing;
        private Arrival arrival;
        private List<Catch> catchList;
        private CommunicationType communicationType;
        private String id;

        public LogbookBuilder withDeparture(Departure departure) {
            this.departure = departure;
            return this;
        }

        public LogbookBuilder withEndOfFishing(EndOfFishing endOfFishing) {
            this.endOfFishing = endOfFishing;
            return this;
        }

        public LogbookBuilder withArrival(Arrival arrival) {
            this.arrival = arrival;
            return this;
        }

        public LogbookBuilder withCatchList(List<Catch> catchList) {
            this.catchList = catchList;
            return this;
        }

        public LogbookBuilder withCommunicationType(CommunicationType communicationType) {
            this.communicationType = communicationType;
            return this;
        }

        public LogbookBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public Logbook build() {
            Logbook logbook = new Logbook();
            logbook.setDeparture(departure);
            logbook.setArrival(arrival);
            logbook.setCatchList(catchList);
            logbook.setCommunicationType(communicationType);
            logbook.setEndOfFishing(endOfFishing);
            logbook.setId(id);
            return logbook;
        }

    }
}
