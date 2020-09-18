package net.endu.enduscan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "edition_race_split")
public class IntermediatePoint implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RACE_ID")
    private Long pathId = Long.valueOf(0);

    @Transient
    private Long locationId = Long.valueOf(0);

    private String field;

    private Integer distance = 0;

    private String name;

    @Transient
    private boolean discriminant = false;
}