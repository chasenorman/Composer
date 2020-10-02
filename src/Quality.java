public class Quality {
    public static final String PERFECT_STRING = "P";
    public static final String MAJOR_STRING = "M";
    public static final String MINOR_STRING = "m";
    public static final String AUGMENTED_STRING = "A";
    public static final String DIMINISHED_STRING = "d";

    public static final Quality PERFECT = new Quality(0);
    public static final Quality MAJOR = new Quality(1);
    public static final Quality MINOR = new Quality(-1);
    public static final Quality AUGMENTED = new Quality(2);
    public static final Quality DIMINISHED = new Quality(-2);

    private final int id;

    Quality(int accidental, boolean perfect, boolean positive) {
        int temp;
        if (perfect) {
            temp = accidental + Integer.signum(accidental);
        } else {
            if (accidental < 0) {
                temp = accidental;
            } else {
                temp = accidental + 1;
            }
        }
        id = temp*(positive ? 1 : -1);
    }

    private Quality(int id) {
        this.id = id;
    }

    public String toString() {
        switch (id) {
            case 0: return PERFECT_STRING;
            case 1: return MAJOR_STRING;
            case -1: return MINOR_STRING;
            default: return id < 0 ? DIMINISHED_STRING.repeat(-id - 1) : AUGMENTED_STRING.repeat(id - 1);
        }
    }

    public boolean equals(Object o) {
        return (o instanceof Quality) && ((Quality) o).id == id;
    }

    public boolean uncommon() {
        return id > 2 || id < -2;
    }
}
