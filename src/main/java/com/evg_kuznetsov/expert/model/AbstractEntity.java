package com.evg_kuznetsov.expert.model;

import com.evg_kuznetsov.expert.model.entities.EntityListener;
import org.springframework.data.domain.Persistable;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(EntityListener.class)
public abstract class AbstractEntity<ID> implements Persistable<ID> {

    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void prePersist() {
        this.markNowNew();
    }

    public void postLoad() {
        this.markNowNew();
    }

    private void markNowNew() {
        this.isNew = false;
    }

    public void preUpdate() {
        //default behavior
    }
}
