package utils;

public class Interval implements Comparable<Interval>{
    public double start;
    public double end;
    public double length;

    public Interval(double start, double length) {
        this.start = start;
        this.length = length;
        end = start + length;
    }

    public int compareTo(Interval i) {
        if (length > i.length)
            return 1;
        if (length < i.length)
            return -1;
        return Double.compare(start, i.start);
    }

    public int hashCode() {
        return Double.hashCode(start) + 199 * Double.hashCode(length);
    }
}
