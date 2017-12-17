import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class AStar {
    Graph<Coords, DefaultWeightedEdge> graph;

    // Contains Set of Vertex already expanded
    Set<Coords> closed;
    // Contains the real cost of the Vertices
    HashMap<Coords, Double> gScore;
    // Contains the predecessor of a Vertex
    HashMap<Coords, DefaultWeightedEdge> cameFrom;
    // Keeps priority for choosing which Vertex
    // to expand next
    PriorityQueue<Coords> priority;

    comparator compar;

    public AStar(Graph<Coords, DefaultWeightedEdge> graph,
                 Coords target) {
        this.graph = graph;
        this.compar = new comparator(gScore, target);
    }

    private void initiliase() {
        closed = new HashSet<>();
        gScore = new HashMap<>();
        cameFrom = new HashMap<>();
        priority = new PriorityQueue<Coords>(1, compar);
    }

    public ArrayList<Coords> getShortestPath(
            Coords source, Coords target) {
        initiliase();
        gScore.put(source, 0.0);
        priority.add(source);

        do {
            Coords currentNode = priority.poll();

            if (currentNode.equals(target)) {
                return this.buildPathList(source, target);
            }

            expandNode(currentNode, target);
            closed.add(currentNode);
        } while (!priority.isEmpty());

        return null;
    }

    private void expandNode(Coords currentNode, Coords target) {
        Set<DefaultWeightedEdge> outgoingEdges =
                graph.edgesOf(currentNode);

        for (DefaultWeightedEdge edge : outgoingEdges) {
            Coords successor = getOppositeVertex(currentNode, edge);
            if (successor == currentNode || closed.contains(currentNode)) continue;
            double hScore = gScore.get(currentNode) +
                    graph.getEdgeWeight(edge);

            if (gScore.containsKey(successor)) {
                if (hScore < gScore.get(successor)) {
                    cameFrom.put(successor, edge);
                    gScore.put(successor, hScore);
                    compar.gScore = gScore;
                    priority.add(successor);
                }
            } else {
                cameFrom.put(successor, edge);
                gScore.put(successor, hScore);
                compar.gScore = gScore;
                priority.add(successor);
            }
        }
    }

    private Coords getOppositeVertex(Coords curr, DefaultWeightedEdge edge) {
        if (graph.getEdgeSource(edge).equals(curr)) {
            return graph.getEdgeTarget(edge);
        }
        return graph.getEdgeSource(edge);
    }

    private ArrayList<Coords> buildPathList(Coords source,
                                            Coords target) {
        ArrayList<Coords> vertexList = new ArrayList<>();
        vertexList.add(target);
        Coords current = target;
        while (current != source) {
            current = getOppositeVertex(current, cameFrom.get(current));
            vertexList.add(current);
        }
        return vertexList;
    }
}
