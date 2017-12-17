import java.util.Comparator;
import java.util.HashMap;

public class comparator implements Comparator<Coords> {
    HashMap<Coords, Double> gScore;
    Coords target;
    public comparator(HashMap<Coords, Double> gScore, Coords target) {
        this.gScore = gScore;
        this.target = target;
    }

    public int compare(Coords node1, Coords node2) {
        return Double.compare(
                gScore.get(node1) + heuristicCost(node1, this.target),
                gScore.get(node2) + heuristicCost(node2, this.target));
    }

    private double heuristicCost(Coords source, Coords target) {
        return Math.sqrt(Math.pow((source.x - target.x), 2)
                + Math.pow((source.y - target.y), 2));
    }

    public void setScore(HashMap<Coords, Double> gScore) {
        this.gScore = gScore;
    }
}
