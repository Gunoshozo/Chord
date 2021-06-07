package ChordModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChordNode implements Serializable {
    private List<Finger> fingerTable;
    private ChordNode predecessor;


    public Integer getId() {
        return id;
    }

    private Integer id;

    public ChordNode findSuccessor(Integer id) {
        return findPredecessor(id).getSuccessor();
    }

    public ChordNode findPredecessor(Integer id) {
        ChordNode n = this;
        //while id not in (n, n.successor]
        while (!isInRange(id, n.id, n.getSuccessor().id, false, true, true)) {
            n = n.closestPrecedingFinger(id);
        }
        return n;
    }

    // this - is node that joins network
    public void join(ChordNode node) {
        if (node != null) {
            this.initFingerTable(node);
            this.updateOthers();
        } else {
            for (int i = 0; i < ChordArray.m; i++) {
                this.getFinger(i).setNode(this);
            }
            this.predecessor = this;
        }
    }

    public void initFingerTable(ChordNode node) {
        this.getFinger(0).setNode(node.findSuccessor(this.getFinger(0).getStart()));
        this.predecessor = this.getSuccessor().getPredecessor();
        this.getSuccessor().predecessor = this;
        for (int i = 0; i < ChordArray.m - 2; i++) {
            if (isInRange(this.getFinger(i + 1).getStart(), this.id, getFinger(i).getNode().id, true, false, true)) {
                this.getFinger(i + 1).setNode(this.getFinger(i).getNode());
            } else {
                this.getFinger(i + 1).setNode(node.findSuccessor(getFinger(i + 1).getStart()));
            }
        }
    }

    public void updateOthers() {
        for (int i = 0; i < ChordArray.m - 1; i++) {
            ChordNode p = this.findPredecessor(this.id - (1 << (i)));
            p.updateFingerTable(this, i);
        }
    }

    private void updateFingerTable(ChordNode s, int i) {
        if (isInRange(s.id, this.id, getFinger(i).getNode().getId(), true, false, true)) {
            this.getFinger(i).setNode(s);
            ChordNode p = this.getPredecessor();
            if (p != s)
                p.updateFingerTable(s, i);
        }
    }

    public void remove() {
        this.getSuccessor().setPredecessor(this.getPredecessor());
        for (int i = 0; i < ChordArray.m - 1; i++) {
            ChordNode p = this.findSuccessor(this.id - (1 << (i)));
            p.updateFingerTableForRemove(this, i);
        }
    }

    private void updateFingerTableForRemove(ChordNode s, int i) {
        if (this.getFinger(i).getNode().getId() == s.getId()) {
            this.getFinger(i).setNode(s.getPredecessor());
            ChordNode p = this.getPredecessor();
            if (p != s)
                p.updateFingerTable(s, i);
        }
    }


    public ChordNode closestPrecedingFinger(Integer id) {
        for (int i = ChordArray.m - 2; i > -1; i--) {
            Integer fingerINodeId = this.getFinger(i).getNode().id;
            if (isInRange(fingerINodeId, this.id, id, false, false, true)) {
                return this.getFinger(i).getNode();
            }
        }
        return this;
    }

    public ChordNode(Integer id) {
        this.id = id;
        this.fingerTable = new ArrayList<>();
    }

    public ChordNode getSuccessor() {
        return this.fingerTable.get(0).getNode();
    }

    public List<Finger> getFingerTable() {
        return fingerTable;
    }

    public Finger getFinger(int i) {
        return fingerTable.get(i);
    }

    public ChordNode deepCopy() {
        ChordNode res = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            oos.close();
            byte[] bytes = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            res = (ChordNode) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static boolean isInRange(int id, int left, int right, boolean includeLeft, boolean includeRight, boolean cyrcular) {
        if (left != right) {
            boolean f1 = false;
            boolean f2 = false;
            if (includeLeft)
                f1 = id >= left;
            else {
                f1 = id > left;
            }
            if (includeRight)
                f2 = id <= right;
            else {
                f2 = id < right;
            }
            if (left < right)
                return f1 && f2;
            else if (cyrcular) {
                return f1 || f2;
            } else {
                return false;
            }
        } else {
            return id == left && includeLeft && includeRight || left == 0;
        }
    }

    public ChordNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(ChordNode predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        String res = "\n============================================================\n"
                + "|| Node id: " + this.id + "\n|| Finger table:\n";
        for (int i = 0; i < this.fingerTable.size(); i++) {
            Finger finger = this.fingerTable.get(i);
            res += "|| start: " + finger.getStart() + " interval: [" + finger.getIntervalLeft() +
                    "," + finger.getIntervalRight() + ") " + "succ: " + finger.getNode().id + "\n";
        }
        res += "============================================================";
        return res;
    }


}
