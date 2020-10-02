public class Key {
    public static final String[] NOTE_NAMES = new String[]{"C", "D", "E", "F", "G", "A", "B"};
    public static final String[] NOTE_NAMES_MINOR = new String[]{"c", "d", "e", "f", "g", "a", "b"};
    public static final int[] MAJOR_SEMITONES = new int[]{0, 2, 4, 5, 7, 9, 11};

    public static final Key C = new Key(0, 0, true);
    public static final Key F = new Key(3, 5, true);
    public static final Key Bb = new Key(6, 10,true);
    public static final Key Eb = new Key(2, 3, true);
    public static final Key Ab = new Key(5, 8, true);
    public static final Key Db = new Key(1, 1, true);
    public static final Key Gb = new Key(4, 6, true);
    public static final Key Cb = new Key(0, 11, true);
    public static final Key G = new Key(4, 7, true);
    public static final Key D = new Key(1, 2,true);
    public static final Key A = new Key(5, 9, true);
    public static final Key E = new Key(2, 4, true);
    public static final Key B = new Key(6, 11, true);
    public static final Key F$ = new Key(3, 6, true);
    public static final Key C$ = new Key(0, 1, true);

    public static final Key a = new Key(5, 9, false);
    public static final Key e = new Key(2, 4, false);
    public static final Key b = new Key(6, 11, false);
    public static final Key f$ = new Key(3, 6, false);
    public static final Key c$ = new Key(0, 1, false);
    public static final Key g$ = new Key(4, 8, false);
    public static final Key d$ = new Key(1, 3, false);
    public static final Key a$ = new Key(5, 10, false);
    public static final Key d = new Key(1, 2, false);
    public static final Key g = new Key(4, 7, false);
    public static final Key c = new Key(0, 0, false);
    public static final Key f = new Key(3, 5, false);
    public static final Key bb = new Key(6, 10, false);
    public static final Key eb = new Key(2, 3, false);
    public static final Key ab = new Key(5, 8, false);

    public static final Key[] DEFAULTS = {C, G, D, A, E, B, F$, F, Bb, Eb, Ab, Db,
            a, e, b, f$, c$, g$, eb, bb, f, c, g, d};

    public static final int MAX_OFFSET = 12;
    public static final int MAX_NUMBER = 7;

    Interval intervalFromC;
    boolean major;

    Key(int number, int offset, boolean major) {
        this.intervalFromC = new Interval(number, offset).octaveMod();
        this.major = major;
    }

    Key(Interval intervalFromC, boolean major) {
        this.intervalFromC = intervalFromC.octaveMod();
        this.major = major;
    }

    public String toString() {
        String total = major ? NOTE_NAMES[intervalFromC.number] : NOTE_NAMES_MINOR[intervalFromC.number];
        int accidental = intervalFromC.semitones - MAJOR_SEMITONES[intervalFromC.number];
        Accidental doubleAccidental = accidental < 0 ? Accidental.DOUBLE_FLAT : Accidental.DOUBLE_SHARP;
        Accidental singleAccidental = accidental < 0 ? Accidental.FLAT : Accidental.SHARP;
        accidental = Math.abs(accidental);

        total += doubleAccidental.toString().repeat(accidental/2);
        if (accidental % 2 == 1) {
            total += singleAccidental.toString();
        }
        return total;
    }

    public int offset(Degree d) {
        return Math.floorMod(intervalFromC.semitones + d.intervalFromRoot.semitones, Key.MAX_OFFSET);
    }

    public boolean equals(Object o) {
        return (o instanceof Key) && ((Key) o).intervalFromC.equals(intervalFromC) && ((Key) o).major == major;
    }

    public Key underModulation(SecondaryProfile profile) {
        return new Key(intervalFromC.add(profile.modulation.intervalFromRoot), profile.major);
    }

    public Key parallel() {
        return new Key(intervalFromC, !major);
    }
}
