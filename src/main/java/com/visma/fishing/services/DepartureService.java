package com.visma.fishing.services;

import com.visma.fishing.model.Departure;

import java.util.List;

public interface DepartureService extends Service<Departure, String> {
    List<Departure> findByPort(String port);
    List<Departure> findByPeriod(String start, String end);
}
