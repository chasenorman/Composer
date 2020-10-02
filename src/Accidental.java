public class Accidental {
    public static final String DOUBLE_FLAT_STRING = "\uD834\uDD2B";
    public static final String DOUBLE_SHARP_STRING = "\uD834\uDD2A";
    public static final String NATURAL_STRING = "♮";
    public static final String SHARP_STRING = "♯";
    public static final String FLAT_STRING = "♭";

    public static final Accidental NATURAL = new Accidental(0);
    public static final Accidental SHARP = new Accidental(1);
    public static final Accidental FLAT = new Accidental(-1);
    public static final Accidental DOUBLE_FLAT = new Accidental(-2);
    public static final Accidental DOUBLE_SHARP = new Accidental(2);

    public int accidental;

    Accidental(int accidental) {
        this.accidental = accidental;
    }

    public String toString() {
        return accidental == 0 ? NATURAL_STRING : toStringNaturalOmitted();
    }

    public String toStringNaturalOmitted() {
        String result = (accidental < 0 ? DOUBLE_FLAT_STRING : DOUBLE_SHARP_STRING).repeat(Math.abs(accidental)/2);
        if ((accidental & 1) == 1) {
            result += (accidental < 0 ? FLAT_STRING : SHARP_STRING);
        }
        return result;
    }

    public boolean equals(Object o) {
        return (o instanceof Accidental) && ((Accidental) o).accidental == accidental;
    }
}
