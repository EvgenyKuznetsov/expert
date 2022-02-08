package com.evg_kuznetsov.expert.model.entities;

import com.evg_kuznetsov.expert.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "service")
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Service extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "naming", length = 20)
    @NotBlank
    @Length(min = 3, max = 20)
    private String naming;

    @ManyToMany(mappedBy = "services")
    private List<Order> orders;


}