package net.endu.enduscan.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "edition_scan_operator_assignment")
public class OperatorAssignment implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long locationId;
    private Long operatorId;

    public OperatorAssignment(Long locationId, Long operatorId) {
        this.locationId = locationId;
        this.operatorId = operatorId;
    }
}
