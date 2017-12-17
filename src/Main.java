import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Throwable {
        CSV fileReader = new CSV("out/production/smartTaxi/nodes.csv",
                "out/production/smartTaxi/taxis.csv",
                "out/production/smartTaxi/client.csv");
        WeightedGraph<Coords, DefaultWeightedEdge> graph = fileReader.createGraph();
        HashMap<Coords, Integer> taxis = fileReader.findTaxis();
        Coords client = fileReader.findClient();

        double sum, min = -1;
        Coords minId = null;
        List<Coords> minVertex = new ArrayList<>();
        PrintWriter writer = new PrintWriter("coords.txt", "UTF-8");
        for (Coords taxi : taxis.keySet()) {
            sum = 0;

            AStar instance = new AStar(graph, client);
            List<Coords> vertexList = instance.getShortestPath(taxi, client);
            writer.println(Float.toString(taxis.get(taxi)));
            for(Coords coord : vertexList) {
                writer.println(Float.toString(coord.x)+","+Float.toString(coord.y));
            }
            for (int i = 1; i < vertexList.size(); i++) {
                DefaultWeightedEdge edge = graph.getEdge(vertexList.get(i), vertexList.get(i - 1));
               sum += graph.getEdgeWeight(edge);
            }
            if (min == - 1) {
                min = sum;
                minId = taxi;
                minVertex = vertexList;
            }
            else if (min > sum) {
                min = sum;
                minId = taxi;
                minVertex = vertexList;
            }
            writer.println();
        }
        System.out.println(min + " " + taxis.get(minId) + " " + minId.x + " " + minId.y);

        writer.println("Min vertex!");
        for(Coords coord : minVertex) {
            writer.println(Float.toString(coord.x) +","+ Float.toString(coord.y));
        }
        writer.close();
    }
}
