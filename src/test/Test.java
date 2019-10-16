package test;

import main.Graph;
import main.Vertex;

public class Test {

    public static void main(String [ ] args)
    {
        Graph graph = new Graph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");
        graph.addVertex("H");
        graph.addVertex("K");

        graph.addEdge("A", "K");
        graph.addEdge("A", "B");
        graph.addEdge("A", "D");
        graph.addEdge("A", "C");
        graph.addEdge("A", "E");
        graph.addEdge("B", "D");
        graph.addEdge("C", "F");
        graph.addEdge("C", "E");
        graph.addEdge("D", "H");
        graph.addEdge("H", "F");
        graph.addEdge("H", "G");
        graph.addEdge("G", "K");

        Vertex v1 = new Vertex("A");
        Vertex v2 = new Vertex("G");

        graph.printAllPaths("A", "G");
    }
}
