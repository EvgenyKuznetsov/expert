package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class EntityListener {

    @PrePersist
    private <T extends AbstractEntity<? extends Number>> void prePersist(T entity) {
        entity.prePersist();
    }

    @PostLoad
    private <T extends AbstractEntity<? extends Number>> void postLoad(T entity) {
        entity.postLoad();
    }

    @PreUpdate
    private <T extends AbstractEntity<? extends Number>> void preUpdate(T entity) {
        entity.preUpdate();
    }
}
