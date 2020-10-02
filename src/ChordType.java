public enum ChordType {
    TONIC("T"), PREDOMINANT("PD"), DOMINANT("D"), SECONDARY_DOMINANT("V/"), SECONDARY_LEADING("vii/"), OTHER("_");

    public final String str;

    ChordType(String str) {
        this.str = str;
    }
}
