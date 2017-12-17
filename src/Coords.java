public class Coords {
    float x, y;
    public Coords(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Coords coords) {
        return Math.sqrt(Math.pow((this.x - coords.x), 2) + Math.pow((this.y - coords.y), 2));
    }

    public boolean equals(Coords coords) {
        if (this.x == coords.x && this.y == coords.y) {
            return true;
        }
        return false;
    }

    public String toString() {
        return Float.toString(this.x) + " " + Float.toString(this.y);
    }

}