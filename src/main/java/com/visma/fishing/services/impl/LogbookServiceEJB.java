package com.visma.fishing.services.impl;

import com.visma.fishing.model.CommunicationType;
import com.visma.fishing.model.Logbook;
import com.visma.fishing.services.LogbookService;
import com.visma.fishing.strategy.DBSavingStrategy;
import com.visma.fishing.strategy.FileSavingStrategy;
import com.visma.fishing.strategy.SavingStrategy;
import io.xlate.inject.Property;
import io.xlate.inject.PropertyResource;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.visma.fishing.auxiliary.Messages.*;
import static com.visma.fishing.queries.Queries.*;

@Transactional
@Stateless
@Slf4j
public class LogbookServiceEJB implements LogbookService {

    @Inject
    @Property(name = "databasePath",
            resource = @PropertyResource("classpath:application.properties"),
            defaultValue = "C:\\dev\\database\\")
    private String databasePath;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Logbook> findAll() {
        TypedQuery<Logbook> q = em.createNamedQuery("logbook.findAll", Logbook.class);
        return q.getResultList();
    }

    @Override
    public Optional<Logbook> findById(String id) {
        return Optional.ofNullable(em.find(Logbook.class, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Logbook> findByDeparturePort(String port) {
        return em.createNativeQuery(
                LOGBOOK_FIND_BY_DEPARTURE_PORT, Logbook.class)
                .setParameter(1,  "%" + port)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Logbook> findByArrivalPort(String port) {
        return em.createNativeQuery(
                LOGBOOK_FIND_BY_ARRIVAL_PORT, Logbook.class)
                .setParameter(1, "%" + port)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Logbook> findBySpecies(String species) {
        return em.createNativeQuery(
                LOGBOOK_FIND_BY_SPECIES, Logbook.class)
                .setParameter(1, "%" + species)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Logbook> findByWeight(Long weight, boolean searchWithLowerWeight) {
        if(searchWithLowerWeight) {
            return em.createNativeQuery(
                    LOGBOOK_FIND_WHERE_WEIGHT_IS_LOWER, Logbook.class)
                    .setParameter(1, weight)
                    .getResultList();
        }
        return em.createNativeQuery(
                LOGBOOK_FIND_WHERE_WEIGHT_IS_BIGGER, Logbook.class)
                .setParameter(1, weight)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Logbook> findByDeparturePeriod(String start, String end){
        return em.createNativeQuery(
                LOGBOOK_FIND_BY_DEPARTURE_DATE, Logbook.class)
                .setParameter(1, start)
                .setParameter(2, end)
                .getResultList();
    }

    @Override
    public Logbook create(Logbook logbook) {
        SavingStrategy savingStrategy;
        if (logbook.getCommunicationType() == CommunicationType.NETWORK) {
            savingStrategy = new DBSavingStrategy(em);
        } else {

            savingStrategy = new FileSavingStrategy(databasePath);
        }
        savingStrategy.save(logbook);
        if(savingStrategy instanceof DBSavingStrategy) {
            log.info(LOGBOOK_SAVE_SUCCESS_MSG + logbook.getId() + TO_DATABASE);
        } else {
            log.info(LOGBOOK_SAVE_SUCCESS_MSG + logbook.getId() + TO_FILE_SYSTEM);
        }
        return logbook;
    }

    @Override
    public Optional<Logbook> updateLogbookById(String id, Logbook logbook) {
        Logbook entity = em.find(Logbook.class, id);
        if (entity == null) {
            return Optional.empty();
        }
        entity = new Logbook.LogbookBuilder().withArrival(logbook.getArrival())
                .withDeparture(logbook.getDeparture())
                .withCatchList(logbook.getCatchList())
                .withDeparture(logbook.getDeparture())
                .withEndOfFishing(logbook.getEndOfFishing())
                .withCommunicationType(logbook.getCommunicationType()).build();
        entity.setEndOfFishing(logbook.getEndOfFishing());
        em.merge(entity);
        log.info(LOGBOOK_SAVE_SUCCESS_MSG + entity.getId() + ".");
        return Optional.of(entity);
    }

    @Override
    public void remove(String id) {
        Optional<Logbook> optional = Optional.ofNullable(em.find(Logbook.class, id));
        optional.ifPresent(entity -> {
            em.remove(entity);
            log.info(LOGBOOK_REMOVED_SUCCESS_MSG + entity.getId());
                });
    }

    @Override
    public List<Logbook> saveAll(List<Logbook> logbooks) {
        for (Logbook logbook :
                logbooks) {
            System.out.println(logbook);
            em.persist(logbook);
        }
        return logbooks;
    }
}
