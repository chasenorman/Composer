public enum Chord {
    // major 7 rare but allowable. vii written as diminished if not seventh. VII cant have 7.
    // 7 isnt resolved.
    // identical spelling of V/iv and V/IV
    // weird voicing of A6
    // We didn't learn N6 7th.
    I(Degree._1, Degree._3, Degree._5, Degree._7, true, ChordType.TONIC, "I"),
    ii(Degree._2, Degree._4, Degree._6, Degree._1, true, ChordType.PREDOMINANT,"ii"),
    iii(Degree._3, Degree._5, Degree._7, Degree._2, true, ChordType.OTHER, "iii"),
    IV(Degree._4, Degree._6, Degree._1, Degree._3, true, ChordType.PREDOMINANT, "IV"),
    V(Degree._5, Degree._7, Degree._2, Degree._4, true, ChordType.DOMINANT, "V"),
    vi(Degree._6, Degree._1, Degree._3, Degree._5, true, ChordType.TONIC, "vi"),
    viihalfdim(Degree._7, Degree._2, Degree._4, Degree._6, true, ChordType.DOMINANT, "viiø"),

    // TODO picardy 1?
    i(Degree._1, Degree.b3, Degree._5, Degree.b7, false, ChordType.TONIC, "i"),
    iihalfdim(Degree._2, Degree._4, Degree.b6, Degree._1, false, ChordType.PREDOMINANT,"iiø"),
    III(Degree.b3, Degree._5, Degree.b7, Degree._2, false, ChordType.OTHER, "III"),
    iv(Degree._4, Degree.b6, Degree._1, Degree.b3, false, ChordType.PREDOMINANT, "iv"),
    VI(Degree.b6, Degree._1, Degree.b3, Degree._5, false, ChordType.TONIC, "VI"),
    VII(Degree.b7, Degree._2, Degree._4, Degree.b6, false, ChordType.OTHER, "VII") /*no 7*/,
    viidim(Degree._7, Degree._2, Degree._4, Degree.b6, false, ChordType.DOMINANT, "viio"),
    V_MINOR(Degree._5, Degree._7, Degree._2, Degree._4, false, ChordType.DOMINANT,"V"),
    // TODO iiiaug, minor ii?

    V_ii(Degree._6, Degree.$1, Degree._3, Degree._5, true, ChordType.SECONDARY_DOMINANT, "V/ii"),
    V_iii(Degree._7, Degree.$2, Degree.$4, Degree._6, true, ChordType.SECONDARY_DOMINANT, "V/iii"),
    V_IV(Degree._1, Degree._3, Degree._5, Degree.b7, true, ChordType.SECONDARY_DOMINANT, "V/IV"),
    V_V(Degree._2, Degree.$4, Degree._6, Degree._1, true, ChordType.SECONDARY_DOMINANT, "V/V"),
    V_vi(Degree._3, Degree.$5, Degree._7, Degree._2, true, ChordType.SECONDARY_DOMINANT, "V/vi"),

    V_III(Degree.b7, Degree._2, Degree._4, Degree.b6, false, ChordType.SECONDARY_DOMINANT, "V/III"),
    V_iv(Degree._1, Degree._3, Degree._5, Degree.b7, false, ChordType.SECONDARY_DOMINANT, "V/iv"),
    V_V_MINOR(Degree._2, Degree.$4, Degree._6, Degree._1, false, ChordType.SECONDARY_DOMINANT, "V/V"),
    V_VI(Degree.b3, Degree._5, Degree.b7, Degree.b2, false, ChordType.SECONDARY_DOMINANT,"V/VI"),
    V_VII(Degree._4, Degree._6, Degree._1, Degree.b3, false, ChordType.SECONDARY_DOMINANT, "V/VII"),

    viidim_ii(Degree.$1, Degree._3, Degree._5, Degree.b7,true, ChordType.SECONDARY_LEADING, "viio/ii"),
    viidim_iii(Degree.$2, Degree.$4, Degree._6, Degree._1, true, ChordType.SECONDARY_LEADING, "viio/iii"),
    viidim_IV(Degree._3, Degree._5, Degree.b7, Degree.b2, true, ChordType.SECONDARY_LEADING,"viio/IV"),
    viidim_V(Degree.$4, Degree._6, Degree._1, Degree.b3, true, ChordType.SECONDARY_LEADING,"viio/V"),
    viidim_vi(Degree.$5, Degree._7, Degree._2, Degree._4, true, ChordType.SECONDARY_LEADING,"viio/vi"),

    viidim_III(Degree._2, Degree._4, Degree.b6, Degree.b1, false, ChordType.SECONDARY_LEADING, "viio/III"),
    viidim_iv(Degree._3, Degree._5, Degree.b7, Degree.b2, false, ChordType.SECONDARY_LEADING, "viio/iv"),
    viidim_V_MINOR(Degree.$4, Degree._6, Degree._1, Degree.b3, false, ChordType.SECONDARY_LEADING,"viio/V"),
    viidim_VI(Degree._5, Degree.b7, Degree.b2, Degree.b4, false, ChordType.SECONDARY_LEADING,"viio/VI"),
    viidim_VII(Degree._6, Degree._1, Degree.b3, Degree.b5, false, ChordType.SECONDARY_LEADING, "viio/VII"),

    viihalfdim_ii(Degree.$1, Degree._3, Degree._5, Degree._7, true, ChordType.SECONDARY_LEADING, "viiø/ii"),
    viihalfdim_IV(Degree._3, Degree._5, Degree.b7, Degree._2, true, ChordType.SECONDARY_LEADING, "viiø/IV"),
    viihalfdim_V(Degree.$4, Degree._6, Degree._1, Degree._3, true, ChordType.SECONDARY_LEADING, "viiø/V"),

    viihalfdim_III(Degree._2, Degree._4, Degree.b6, Degree._1, false, ChordType.SECONDARY_LEADING, "viiø/III"),
    viihalfdim_iv(Degree._3, Degree._5, Degree.b7, Degree._2, false, ChordType.SECONDARY_LEADING, "viiø/iv"),
    viihalfdim_VI(Degree._5, Degree.b7, Degree.b2, Degree._4, false, ChordType.SECONDARY_LEADING, "viiø/VI"),
    viihalfdim_VII(Degree._6, Degree._1, Degree.b3, Degree._5, false, ChordType.SECONDARY_LEADING, "viiø/VII"),

    N6(Degree.b2, Degree._4, Degree.b6, Degree._1, false, ChordType.OTHER, "N6"),
    A6(Degree.$4, Degree.b6, Degree._1, Degree.b3, false, ChordType.OTHER, "A6"),
    Fr6(Degree._2, Degree.$4, Degree.b6, Degree._1, false, ChordType.OTHER, "Fr6"),

    v(Degree._5, Degree.b7, Degree._2, Degree._4, false, ChordType.OTHER, "v"),
    vidim(Degree._6, Degree._1, Degree.b3, Degree._5, false, ChordType.OTHER, "vio"),
    IV_MINOR(Degree._4, Degree._6, Degree._1, Degree._3, false, ChordType.PREDOMINANT, "IV"); // predominant

    static {
        V_ii.profile = SecondaryProfile.ii;
        V_iii.profile = SecondaryProfile.iii;
        V_IV.profile = SecondaryProfile.IV;
        V_V.profile = SecondaryProfile.V;
        V_vi.profile = SecondaryProfile.vi;

        V_III.profile = SecondaryProfile.III;
        V_iv.profile = SecondaryProfile.iv;
        V_V_MINOR.profile = SecondaryProfile.v;
        V_VI.profile = SecondaryProfile.VI;
        V_VII.profile = SecondaryProfile.VII;

        viidim_ii.profile = SecondaryProfile.ii;
        viidim_iii.profile = SecondaryProfile.iii;
        viidim_IV.profile = SecondaryProfile.IV;
        viidim_V.profile = SecondaryProfile.V;
        viidim_vi.profile = SecondaryProfile.vi;

        viidim_III.profile = SecondaryProfile.III;
        viidim_iv.profile = SecondaryProfile.iv;
        viidim_V_MINOR.profile = SecondaryProfile.v;
        viidim_VI.profile = SecondaryProfile.VI;
        viidim_VII.profile = SecondaryProfile.VII;

        viihalfdim_ii.profile = SecondaryProfile.ii;
        viihalfdim_IV.profile = SecondaryProfile.IV;
        viihalfdim_V.profile = SecondaryProfile.V;

        viihalfdim_III.profile = SecondaryProfile.III;
        viihalfdim_iv.profile = SecondaryProfile.iv;
        viihalfdim_VI.profile = SecondaryProfile.VI;
        viihalfdim_VII.profile = SecondaryProfile.VII;
    }

    public final Degree root;
    public final Degree third;
    public final Degree fifth;
    public final Degree seventh;

    public final Degree[] notes;

    public final boolean major;
    public final String str;

    public final ChordType type;
    public SecondaryProfile profile;


    Chord(Degree root, Degree third, Degree fifth, Degree seventh, boolean major, ChordType type, String str) {
        this.root = root;
        this.third = third;
        this.fifth = fifth;
        this.seventh = seventh;
        this.str = str;
        this.major = major;
        this.type = type;
        this.notes = new Degree[]{root, third, fifth, seventh};
    }

    public String toString() {
        return str;
    }
}