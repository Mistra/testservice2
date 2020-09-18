package net.endu.enduscan.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "edition_race")
public class Path implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long editionId; 
    private String name;
    private int distance;
    private Integer classified;
    private int sorting;

    public boolean getPublished() {
        return false;
    }

    public String getRankingOrder() {
        return (sorting == 1) ? "alphabetical" : "absolute";
    }

    public void setPublished(boolean published) {
        classified = published ? 1 : 0;
    }

    public void setRankingOrder(String rankingOrder) {
        sorting = rankingOrder.equals("alphabetical") ? 1 : 0;
    }
}
