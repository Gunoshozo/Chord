package ChordModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChordArray {
    private List<ChordNode> nodeList;
    public static Integer m;

    public ChordArray(List<Integer> m_arPos, Integer M_intM) {
        ChordArray.m = M_intM;
        nodeList = new ArrayList<>();
        for (int i = 0; i < m_arPos.size(); i++) {
            nodeList.add(new ChordNode(m_arPos.get(i)));
        }
        Map<Integer, ChordNode> idToChordNode = this.nodeList.stream().collect(Collectors.toMap(ChordNode::getId, Function.identity()));
        for (int i = 0; i < nodeList.size(); i++) {
            ChordNode n = nodeList.get(i);
            int id = m_arPos.get(i);
            n.setPredecessor((i == 0) ? nodeList.get(nodeList.size() - 1) : nodeList.get(i - 1));
            for (int j = 0; j < ChordArray.m - 1; j++) {
                int start = (id + (1 << j)) % (1 << (ChordArray.m - 1));
                List<Integer> interval = new ArrayList<>();
                interval.add((id + (1 << j)) % (1 << (ChordArray.m - 1)));
                Integer intervalEnd = (j < ChordArray.m - 2) ? (id + (1 << (j + 1))) % (1 << (ChordArray.m - 1)) : id;
                interval.add(intervalEnd);
                boolean flag = false;
                for (int k = 0; k < m_arPos.size(); k++) {
                    int ind = m_arPos.get(k);
                    if (ind >= start) {
                        n.getFingerTable().add(new Finger(start, interval, idToChordNode.get(ind)));
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    n.getFingerTable().add(new Finger(start, interval, idToChordNode.get(m_arPos.get(0))));
                }
            }
        }
    }

    public void initNode(ChordNode n) {
        for (int j = 0; j < ChordArray.m - 1; j++) {
            int start = (n.getId() + (1 << j)) % (1 << (ChordArray.m - 1));
            List<Integer> interval = new ArrayList<>();
            interval.add((n.getId() + (1 << j)) % (1 << (ChordArray.m - 1)));
            Integer intervalEnd = (j < ChordArray.m - 2) ? (n.getId() + (1 << (j + 1))) % (1 << (ChordArray.m - 1)) : n.getId();
            interval.add(intervalEnd);
            n.getFingerTable().add(new Finger(start, interval));
        }
    }

    public void join(ChordNode node) {
        if (nodeList.size() != 0) {
            ChordNode originNode = nodeList.get(0);
            Integer pos = findPos(node.getId());
            nodeList.add(pos, node);
            node.join(originNode);
        } else {
            nodeList.add(node);
            node.join(null);
        }
    }

    public void join(ChordNode node, int originInd) {
        if (nodeList.size() != 0) {
            ChordNode originNode = nodeList.get(originInd);
            Integer pos = findPos(node.getId());
            nodeList.add(pos, node);
            initNode(node);
            node.join(originNode);
        } else {
            nodeList.add(node);
            initNode(node);
            node.join(null);
        }
    }

    public void remove(ChordNode node) {
        node.remove();
        nodeList.remove(node);
    }


    public List<ChordNode> getNodeList() {
        return nodeList;
    }



    @Override
    public String toString() {
        return nodeList.toString();
    }

    private Integer findPos(int originId) {
        for (int i = 0; i < this.nodeList.size(); i++) {
            if (this.nodeList.get(i).getId() > originId) {
                return i;
            }
        }
        return this.nodeList.size();
    }
}
