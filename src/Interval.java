public class Interval {
    private static final int[] perfectMajor = new int[]{0, 2, 4, 5, 7, 9, 11};
    public static final Interval PERFECT_UNISON = new Interval(0,0);
    public static final Interval PERFECT_FIFTH = new Interval(4, 7);
    public static final Interval AUGMENTED_SECOND = new Interval(1, 3);
    public static final Interval DIMINISHED_SEVENTH = new Interval(6, 9);
    public static final Interval DIMINISHED_FIFTH = new Interval(4, 6);
    public static final Interval MINOR_SECOND = new Interval(1, 1);

    public static final int MAX_NUMBER = 7;
    public static final int MAX_SEMITONES = 12;

    public int number;
    public int semitones;

    public Interval(int number, int semitones) {
        this.number = number;
        this.semitones = semitones;
    }

    public Quality quality() {
        boolean positive = number >= 0;
        Interval mod = octaveMod();
        boolean perfect = mod.number == 0 || mod.number == 3 || mod.number == 4;
        return new Quality(mod.semitones - perfectMajor[mod.number], perfect, positive);
    }

    public Accidental accidental() {
        Interval mod = octaveMod();
        return new Accidental(mod.semitones - perfectMajor[mod.number]);
    }

    public Interval octaveMod() {
        int octaves = Math.floorDiv(number, MAX_NUMBER);
        return new Interval(number - octaves*MAX_NUMBER, semitones - octaves*MAX_SEMITONES);
    }

    public String toString() {
        return quality() + "" + ((number < 0) ? (number-1) : (number+1));
    }

    public Interval add(Interval o) {
        return new Interval(number + o.number, semitones + o.semitones);
    }

    public Interval sub(Interval o) {
        return new Interval(number - o.number, semitones - o.semitones);
    }

    public boolean equals(Object o) {
        return (o instanceof Interval) && ((Interval)o).number == number && ((Interval)o).semitones == semitones;
    }

    public boolean stepwise() {
        return number > -2 && number < 2;
    }
}
