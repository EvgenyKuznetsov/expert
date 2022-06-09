package com.evgKuznetsov.expert.model;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;


@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = "id")
    @Getter
    private Long id;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
