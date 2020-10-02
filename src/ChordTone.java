public enum ChordTone {
    ROOT, THIRD, FIFTH, SEVENTH;

    public String toString() {
        return name();
    }

    public static final int TONES = 4;
    public static final ChordTone[] ORDER = new ChordTone[]{ROOT, THIRD, FIFTH, SEVENTH};
}
