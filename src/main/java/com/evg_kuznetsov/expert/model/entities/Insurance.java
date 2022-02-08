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

@Entity(name = "insurance")
@Table(name = "insurance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Insurance extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "global_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "insurance_type", unique = true, length = 10)
    @NotBlank
    @Length(max = 10)
    private String insuranceType;

    @OneToMany(mappedBy = "typeInsurance")
    private List<Order> orders;

}