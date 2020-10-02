public enum Voice {
    BASS, TENOR, ALTO, SOPRANO;

    public static final int VOICES = 4;

    public static final Voice[] OUTER = new Voice[]{BASS, SOPRANO};

    public boolean withinRange(int midi) {
        return midi >= Test.LOWS[ordinal()] && midi <= Test.HIGHS[ordinal()];
    }
}
