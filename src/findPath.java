import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;
import org.jgrapht.alg.*;
import org.jgrapht.alg.interfaces.*;
//import sun.security.provider.certpath.Vertex;

public class findPath implements AStarAdmissibleHeuristic {

    public double getCostEstimate(Coords s, Coords t){
        return Math.sqrt(Math.pow((s.x - t.x), 2) + Math.pow((s.y - t.y), 2));
    }

    public GraphPath Calculate(SimpleWeightedGraph graph, Coords source, Coords target){

        AStarShortestPath instance = new AStarShortestPath(graph);

        //double heuristic = Math.sqrt(Math.pow((target.x - source.x), 2) + Math.pow((target.y - source.y), 2));
        return instance.getShortestPath(source, target, getCostEstimate(source, target));
    }

    @Override
    public double getCostEstimate(Object s, Object t) {
        return 0;
    }
}
