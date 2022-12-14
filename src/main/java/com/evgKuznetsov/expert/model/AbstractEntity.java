package com.evgKuznetsov.expert.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;


@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
public abstract class AbstractEntity implements Persistable<Long>, IdAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", allocationSize = 1, initialValue = 110)
    @Column(name = "id")
    private Long id;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
