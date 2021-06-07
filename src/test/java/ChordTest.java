import ChordModel.ChordArray;
import ChordModel.ChordNode;
import ChordModel.Finger;
import org.junit.jupiter.api.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChordTest {

    private ChordArray chordArray;
    public List<Integer> testData1 = Arrays.asList(1, 3, 6, 3, 3, 6, 6, 6, 0, 0, 0, 3);
    public List<Integer> testData2 = Arrays.asList(0, 3, 6, 6, 6, 0, 0, 0, 3);

    @AllArgsConstructor
    static class TestData {
        List<Integer> initialList;
        Integer closestPrecedingFingerResult;
        Integer findPredecessorResult;
        Integer findSuccessorResult;
    }


    private final int m = 4;
    private final TestData testData = new TestData(new ArrayList<Integer>() {{
        add(0);
        add(1);
        add(3);
    }}, 3, 3, 0);


    @BeforeEach
    public void setUp() {
        chordArray = new ChordArray(testData.initialList, m);
    }

    @AfterEach
    public void cleanUp() {
        chordArray = null;
    }

    @Test
    @Order(0)
    public void isInRangeTest() {
        boolean test1 = ChordNode.isInRange(2, 1, 3, false, false, true);
        Assertions.assertTrue(test1);
        boolean test2 = ChordNode.isInRange(2, 2, 3, true, false, true);
        Assertions.assertTrue(test2);
        boolean test3 = ChordNode.isInRange(2, 1, 2, false, true, true);
        Assertions.assertTrue(test3);
        boolean test4 = ChordNode.isInRange(2, 3, 1, false, false, true);
        Assertions.assertFalse(test4);
        boolean test5 = ChordNode.isInRange(2, 3, 1, false, false, true);
        Assertions.assertFalse(test5);
        boolean test6 = ChordNode.isInRange(2, 2, 1, true, false, true);
        Assertions.assertTrue(test6);
        boolean test7 = ChordNode.isInRange(2, 7, 2, false, true, true);
        Assertions.assertTrue(test7);
        boolean test8 = ChordNode.isInRange(4, 7, 2, false, true, true);
        Assertions.assertFalse(test8);
    }

    @Test
    @Order(1)
    public void printInitialStateTest() {
        System.out.println(chordArray.toString());
        Assertions.assertTrue(chordArray != null);
    }

    @Test
    @Order(2)
    public void searchFunctionsTest() {
        Integer originId = 1;
        Integer targetId = 5;
        System.out.println("Origin node id: " + originId);
        System.out.println("TargetId id: " + targetId);
        Optional<ChordNode> optional = chordArray.getNodeList().stream().filter(it -> it.getId() == originId).findFirst();
        if (optional.isPresent()) {
            ChordNode originNode = optional.get();
            ChordNode cpf = originNode.closestPrecedingFinger(targetId);
            System.out.println("Closest preceding finger Id: " + cpf.getId());
            ChordNode p = originNode.findPredecessor(targetId);
            System.out.println("Find predecessor Id: " + p.getId());
            ChordNode s = originNode.findSuccessor(targetId);
            System.out.println("Find successor Id: " + s.getId());
            Assertions.assertEquals(testData.closestPrecedingFingerResult, cpf.getId());
            Assertions.assertEquals(testData.findPredecessorResult, p.getId());
            Assertions.assertEquals(testData.findSuccessorResult, s.getId());
        }


    }

    @Test
    @Order(3)
    public void joinTest() {
        Integer originInd = 2;
        Integer toAddId = 6;
        ChordNode newChordNode = new ChordNode(toAddId);
        chordArray.join(newChordNode, originInd);
        System.out.println(chordArray);
        List<Integer> res = new ArrayList<>();
        chordArray.getNodeList().forEach(it -> {
            res.addAll(it.getFingerTable().stream().map(Finger::getNode).map(ChordNode::getId).collect(Collectors.toList()));
        });
        for (int i = 0; i < res.size(); i++) {
            Assertions.assertTrue(testData1.get(i).equals(res.get(i)));
        }
    }

    @Test
    @Order(4)
    public void leaveTest() {
        Integer originInd = 2;
        Integer toAddId = 6;
        ChordNode newChordNode = new ChordNode(toAddId);
        chordArray.join(newChordNode, originInd);
        Integer toLeave = 1;
        Optional<ChordNode> nodeOptional = chordArray.getNodeList().stream().filter(it -> it.getId() == toLeave).findFirst();
        Assertions.assertTrue(nodeOptional.isPresent());
        ChordNode node = nodeOptional.get();
        chordArray.remove(node);
        System.out.println(chordArray);
        List<Integer> res = new ArrayList<>();
        chordArray.getNodeList().forEach(it -> {
            res.addAll(it.getFingerTable().stream().map(Finger::getNode).map(ChordNode::getId).collect(Collectors.toList()));
        });
        for (int i = 0; i < res.size(); i++) {
            Assertions.assertTrue(testData2.get(i).equals(res.get(i)));
        }
    }
}
