package edu.wpi.cs3733d18.teamQ.UnitTests;

import edu.wpi.cs3733d18.teamQ.pathfinding.Astar;
import org.junit.Ignore;
import edu.wpi.cs3733d18.teamQ.pathfinding.Graph;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GraphTest {
    private static Graph testGraph;
    private static ArrayList<Node> testNodes = new ArrayList<Node>();
    private static ArrayList<Edge> testEdges = new ArrayList<Edge>();

    private static Node node1 = new Node("TEST1", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG1", "TEST1", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node2 = new Node("TEST2", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG2", "TEST2", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node3 = new Node("TEST3", 1.0, 100.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG3", "TEST3", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node4 = new Node("TEST4", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG4", "TEST4", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node5 = new Node("TEST5", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG5", "TEST5", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node6 = new Node("TEST6", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG6", "TEST6", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node7 = new Node("TEST7", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG7", "TEST7", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node8 = new Node("TEST8", 1.0, 1.0, 1, "TESTBUILDING", "EXCLUDED",
            "TEST-LONG8", "TEST8", "Team Q", 0.0, 0.0, 0.0, true);
    private static Node node9 = new Node("TEST9", 1.0, 1.0, 1, "TESTBUILDING", "TEST",
            "TEST-LONG9", "TEST9", "Team Q", 0.0, 0.0, 0.0, true);

    private static Edge edge1to2 = new Edge("TEST1-2", node1, node2, 1.0, true);
    private static Edge edge1to8 = new Edge("TEST1-8", node1, node8, 1.0, true);
    private static Edge edge1to3 = new Edge("TEST1-3", node1, node3, 100.0, true);
    private static Edge edge2to4 = new Edge("TEST2-4", node2, node4, 1.0, true);
    private static Edge edge2to9 = new Edge("TEST2-7", node2, node9, 10.0, true);
    private static Edge edge2to5 = new Edge("TEST2-5", node2, node5, 2.0, true);
    private static Edge edge3to4 = new Edge("TEST3-4", node3, node4, 1.0, true);
    private static Edge edge3to5 = new Edge("TEST3-5", node3, node5, 4.0, true);
    private static Edge edge8to9 = new Edge("TEST8-9", node8, node9, 1.0, true);

    /**
     * Initialize the graph and lists for nodes and edges.
     */
    @BeforeClass
    public static void setup(){
        testNodes.add(node1);
        testNodes.add(node2);
        testNodes.add(node3);
        testNodes.add(node4);
        testNodes.add(node5);
        testNodes.add(node6);
        testNodes.add(node8);
        testNodes.add(node9);

        testEdges.add(edge1to2);
        testEdges.add(edge1to3);
        testEdges.add(edge1to8);
        testEdges.add(edge2to4);
        testEdges.add(edge2to5);
        testEdges.add(edge2to9);
        testEdges.add(edge3to4);
        testEdges.add(edge3to5);
        testEdges.add(edge8to9);

        testGraph = new Astar();
        testGraph.init2(testNodes, testEdges);
    }

    /**
     * Tests that two nodes without a connection have no generated path between them.
     */
    @Test
    public void testNoPath(){
        ArrayList<Node> expectedPath = new ArrayList<Node>();
        ArrayList<String> noType = new ArrayList<>();
        ArrayList<Node> fetchedPath = testGraph.findPath(node1, node6, noType);

        assertEquals(expectedPath, fetchedPath);
    }

    /**
     * Tests that a path from the node to itself simply contains the node
     */
    @Test
    public void testTrivialPath(){
        ArrayList<Node> expectedPath = new ArrayList<Node>();
        expectedPath.add(node1);
        ArrayList<String> noType = new ArrayList<>();
        ArrayList<Node> fetchedPath = testGraph.findPath(node1, node1, noType);

        assertEquals(expectedPath, fetchedPath);
    }

    /**
     * Tests that a path that is not a trivial one step is correctly generated, even
     * with another option that would be found sooner.
     */
    @Test @Ignore
    public void testNonTrivalPath(){
        ArrayList<Node> expectedPath = new ArrayList<Node>();
        ArrayList<String> noType = new ArrayList<>();

        expectedPath.add(node1);
        expectedPath.add(node2);
        expectedPath.add(node4);
        expectedPath.add(node3);
        ArrayList<Node> fetchedPath = testGraph.findPath(node1, node3, noType);

        assertEquals(expectedPath, fetchedPath);
    }


    /**
     * Tests that a path that will be correctly generated when avoiding a certain type
     * of node
     */
    @Test
    public void testNoTypePath(){
        //TODO finish this when feature is fully implemented
        ArrayList<Node> expectedPath = new ArrayList<Node>();
        ArrayList<String> noType = new ArrayList<>();
        noType.add(node8.getType());

        expectedPath.add(node1);
        expectedPath.add(node2);
        expectedPath.add(node9);
        ArrayList<Node> fetchedPath = testGraph.findPath(node1, node9, noType);

        assertEquals(expectedPath, fetchedPath);
    }
}
