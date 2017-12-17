import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;
import java.util.HashMap;

public class CSV {
    private String argNodes = "out/production/smartTaxi/nodes.csv";
    private String argTaxis = "out/production/smartTaxi/taxis.csv";
    private String argClient = "out/production/smartTaxi/client.csv";

    public HashMap<String, Coords> nodes = new HashMap<>();

    public CSV(String argNodes, String argTaxis, String argClient) {
        this.argNodes = argNodes;
        this.argTaxis = argTaxis;
        this.argClient = argClient;
        this.nodes = new HashMap<String, Coords>();
    }

    WeightedGraph<Coords, DefaultWeightedEdge> createGraph() throws IOException {
        FileReader file = new FileReader(argNodes);
        WeightedGraph<Coords, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Coords prevCoords = null;
        String prevId = null;
        int count = 0;
        for(CSVRecord csvIter : CSVFormat.EXCEL.parse(file)) {
            if (count == 0) {
                count += 1;
            } else {
                Coords coords = new Coords(Float.parseFloat(csvIter.get(0)), Float.parseFloat(csvIter.get(1)));
                String id = csvIter.get(2);

                if (id.equals(prevId)) {
                    if (nodes.containsKey(coords.toString())) {
                        coords = nodes.get(coords.toString());
                        if (!graph.containsEdge(prevCoords, coords)) {
                            if (!coords.toString().equals(prevCoords.toString())) {
                                DefaultWeightedEdge edge = graph.addEdge(prevCoords, coords);
                                graph.setEdgeWeight(edge, prevCoords.distance(coords));
                            }
                        }
                    } else {
                        nodes.put(coords.toString(), coords);
                        graph.addVertex(coords);
                        DefaultWeightedEdge edge = graph.addEdge(prevCoords, coords);
                        graph.setEdgeWeight(edge, prevCoords.distance(coords));
                    }
                } else {
                    if (nodes.containsKey(coords.toString())) {
                        coords = nodes.get(coords.toString());
                    } else {
                        nodes.put(coords.toString(), coords);
                        graph.addVertex(coords);
                    }
                }
                prevCoords = coords;
                prevId = id;
            }
        }
        return graph;
    }

    HashMap<Coords, Integer> findTaxis() throws IOException {
        FileReader file = new FileReader(argTaxis);
        HashMap<Coords, Integer> taxis = new HashMap<>();
        int count = 0;
        for(CSVRecord csvIter : CSVFormat.EXCEL.parse(file)) {
            if (count == 0) {
                count += 1;
            }
            else {
                Coords coords = new Coords(Float.parseFloat(csvIter.get(0)), Float.parseFloat(csvIter.get(1)));
                int id = Integer.parseInt(csvIter.get(2));

                taxis.put(findMin(coords), id);
            }
        }
        return taxis;
    }

    Coords findClient() throws IOException {
        FileReader file = new FileReader(argClient);
        Coords coords = null;
        int count = 0;
        for(CSVRecord csvIter : CSVFormat.EXCEL.parse(file)) {
            if (count == 0) {
                count += 1;
            }
            else {
                coords = findMin(new Coords(Float.parseFloat(csvIter.get(0)), Float.parseFloat(csvIter.get(1))));
            }
        }
        return coords;
    }

    private Coords findMin(Coords coords) {
        double min = -1;
        Coords minCoords = null;
        for (String key : nodes.keySet()) {
            Coords value = nodes.get(key);
            if (min < 0) {
                min = value.distance(coords);
                minCoords = value;
            }
            else {
                if (min > value.distance(coords)) {
                    min = value.distance(coords);
                    minCoords = value;
                }
            }
        }
        return minCoords;
    }
}