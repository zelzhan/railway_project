import java.util.*;

class Graph {
    private Map<String, List<String>> adjVertices;

    public Graph () {

        adjVertices = new HashMap<>();

    }

    void addEdge(String label1, String label2) {
        adjVertices.get(label1).add(label2);
        adjVertices.get(label2).add(label1);

    }

    void removeEdge(String label1, String label2) {
        List<String> eV1 = adjVertices.get(label1);
        List<String> eV2 = adjVertices.get(label2);
        if (eV1 != null)
            eV1.remove(label2);
        if (eV2 != null)
            eV2.remove(label1);
    }

    List<String> getAdjVertices(String label) {
        return adjVertices.get(label);
    }

    void addVertex(String label) {
        adjVertices.putIfAbsent(label, new ArrayList<>());
    }

    void removeVertex(String label) {
        Vertex v = new Vertex(label);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(label));
    }



    public void printAllPaths(String s, String d)
    {
        HashSet<String> isVisited = new HashSet<>();
        ArrayList<String> pathList = new ArrayList<>();

        //add source to path[]
        pathList.add(s);

        //Call recursive utility
        printAllPathsUtil(s, d, isVisited, pathList);
    }

    private void printAllPathsUtil(String u, String d,
                                   HashSet<String> isVisited,
                                   List<String> localPathList) {

        // Mark the current node
        isVisited.add(u);

        if (u.equals(d))
        {
            System.out.println(localPathList);
            // if match found then no need to traverse more till depth
            isVisited.remove(u);
            return ;
        }

        for (String v : adjVertices.get(u))
        {
            if (!isVisited.contains(v))
            {
                // store current node
                // in path[]
                localPathList.add(v);
                printAllPathsUtil(v, d, isVisited, localPathList);

                // remove current node
                // in path[]
                localPathList.remove(v);
            }
        }

        // Mark the current node
        isVisited.remove(u);
    }
}