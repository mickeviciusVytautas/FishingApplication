package com.visma.fishing.strategy;

import com.visma.fishing.model.Logbook;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

public class DBSavingStrategy implements SavingStrategy {

    EntityManager em;

    public DBSavingStrategy (EntityManager entityManager){
        this.em = entityManager;
    }
    @Override
    public Response save(Logbook logbook) {
        em.persist(logbook);
        return Response.ok("Successfully saved logbook to database.").build();
    }
}
