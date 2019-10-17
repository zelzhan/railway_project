//package test;
//
//import main.Graph;
//import main.Vertex;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class TestGraph {
//
//    Graph graph;
//
//    @Before
//    public void setUp() {
//        graph = new Graph();
//        graph.addVertex("A");
//        graph.addVertex("B");
//        graph.addVertex("C");
//        graph.addVertex("D");
//        graph.addVertex("E");
//        graph.addVertex("F");
//        graph.addVertex("G");
//        graph.addVertex("H");
//        graph.addVertex("K");
//
//        graph.addEdge("A", "K");
//        graph.addEdge("A", "B");
//        graph.addEdge("A", "D");
//        graph.addEdge("A", "C");
//        graph.addEdge("A", "E");
//        graph.addEdge("B", "D");
//        graph.addEdge("C", "F");
//        graph.addEdge("C", "E");
//        graph.addEdge("D", "H");
//        graph.addEdge("H", "F");
//        graph.addEdge("H", "G");
//        graph.addEdge("G", "K");
//
//        Vertex v1 = new Vertex("A");
//        Vertex v2 = new Vertex("G");
//    }
//
//    @Test
//    public void test_remove_edge() {
//        graph.removeEdge("A", "K");
//    }
//
//    @Test
//    public void test_remove_vertex() {
//        graph.removeVertex("B");
//    }
//
//    @Test
//    public void test_print() {
//        graph.printAllPaths("C", "H");
//    }
//
//    @Test
//    public void test_get_adjacent() {
//        List<String> adjacents = new ArrayList<>(Arrays.asList("A", "F", "E"));
//        assertTrue("C is adjacent to D", graph.getAdjVertices("C").equals(adjacents));
//    }
//}
