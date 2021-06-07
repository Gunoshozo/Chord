package ChordModel;

import java.io.Serializable;
import java.util.List;

public class Finger implements Serializable {
    private Integer start;
    private List<Integer> interval;
    // node == finger[i].successor
    private ChordNode node;

    public Finger() {
    }

    public Finger(Integer start, List<Integer> interval) {
        this.start = start;
        this.interval = interval;
    }

    public Finger(Integer start, List<Integer> interval, ChordNode node) {
        this.start = start;
        this.interval = interval;
        this.node = node;
    }

    public ChordNode getNode() {
        return node;
    }

    public void setNode(ChordNode node) {
        this.node = node;
    }

    public Integer getStart() {
        return this.start;
    }

    public Integer getIntervalLeft() {
        return this.interval.get(0);
    }

    public Integer getIntervalRight() {
        return this.interval.get(1);
    }

}
