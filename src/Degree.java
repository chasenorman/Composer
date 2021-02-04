public class Degree {
    public static final int[] majorDiatonic = new int[]{0, 2, 4, 5, 7, 9, 11};
    public static final int[] minorDiatonic = new int[]{0, 2, 3, 5, 7, 8, 10};

    public static final Degree _1 = new Degree(0, 0);
    public static final Degree $1 = new Degree(0, 1);
    public static final Degree b1 = new Degree(0, 11);
    public static final Degree _2 = new Degree(1, 2);
    public static final Degree $2 = new Degree(1, 3);
    public static final Degree b2 = new Degree(1, 1);
    public static final Degree _3 = new Degree(2, 4);
    public static final Degree $3 = new Degree(2, 5);
    public static final Degree b3 = new Degree(2, 3);
    public static final Degree _4 = new Degree(3, 5);
    public static final Degree $4 = new Degree(3, 6);
    public static final Degree b4 = new Degree(3, 4);
    public static final Degree _5 = new Degree(4, 7);
    public static final Degree $5 = new Degree(4, 8);
    public static final Degree b5 = new Degree(4, 6);
    public static final Degree _6 = new Degree(5, 9);
    public static final Degree $6 = new Degree(5, 10);
    public static final Degree b6 = new Degree(5, 8);
    public static final Degree _7 = new Degree(6, 11);
    public static final Degree $7 = new Degree(6, 0);
    public static final Degree b7 = new Degree(6, 10);

    public final Interval intervalFromRoot;

    Degree(int number, int offset) {
        intervalFromRoot = new Interval(number, offset).octaveMod();
    }

    // presuming o is higher? By closest?
    public Interval to(Degree o) {
        return o.intervalFromRoot.sub(intervalFromRoot);
    }

    public String toString() {
        return intervalFromRoot.accidental().toStringNaturalOmitted() + (intervalFromRoot.number+1) + "̂";
    }

    public boolean equals(Object o) {
        return (o instanceof Degree) && ((Degree) o).intervalFromRoot.equals(intervalFromRoot);
    }

    public boolean diatonic(boolean major) {
        return intervalFromRoot.semitones == (major ? majorDiatonic : minorDiatonic)[intervalFromRoot.number];
    }

    String[] notes = {"C", "D", "E", "F", "G", "A", "B"};
    public String noteString(Key key) {
        Interval n = key.intervalFromC.add(intervalFromRoot).octaveMod();
        return notes[n.number] + n.accidental().toStringNaturalOmitted();
    }

    public String noteStringWithOctave(Key key, int midi) {
        int octave = ((midi - intervalFromRoot.accidental().accidental) / 12) - 1;
        return noteString(key) + octave;
    }
}
