import java.util.Arrays;

public enum SecondaryProfile {
    ii(Degree._2, false, new Chord[]{Chord.ii, Chord.viidim, Chord.V_V, Chord.viidim_V, Chord.viihalfdim_V}, new Chord[]{Chord.V}),
    iii(Degree._3, false, new Chord[]{Chord.iii, Chord.I, Chord.V_vi, Chord.viidim_vi}, new Chord[]{Chord.vi, Chord.I}),
    IV(Degree._4, true, new Chord[]{Chord.IV, Chord.ii}, new Chord[]{Chord.ii}),
    V(Degree._5, true, new Chord[]{Chord.V, Chord.iii}, new Chord[]{Chord.vi, Chord.I, Chord.iii}),
    vi(Degree._6, false, new Chord[]{Chord.vi, Chord.IV, Chord.V_ii, Chord.viidim_ii, Chord.viihalfdim_ii}, new Chord[]{Chord.viidim, Chord.ii, Chord.IV}),

    III(Degree.b3, true, new Chord[]{Chord.III, Chord.i, Chord.V_VI, Chord.viidim_VI, Chord.viihalfdim_VI}, new Chord[]{Chord.iv, Chord.VI, Chord.i}),
    iv(Degree._4, false, new Chord[]{Chord.iv, Chord.iihalfdim, Chord.V_VII, Chord.viidim_VII, Chord.viihalfdim_VII}, new Chord[]{}),
    v(Degree._5, false, new Chord[]{Chord.V_MINOR, Chord.v, Chord.III}, new Chord[]{Chord.i, Chord.III}),
    VI(Degree.b6, true, new Chord[]{Chord.VI, Chord.iv, Chord.N6}, new Chord[]{Chord.iv}),
    VII(Degree.b7, true, new Chord[]{Chord.VII, Chord.V_MINOR, Chord.v, Chord.V_III, Chord.viidim_III, Chord.viihalfdim_III}, new Chord[]{Chord.i, Chord.III, Chord.v});

    public final Degree modulation;
    public final boolean major;
    public final Chord[] resolutions;
    public final Chord[] pivots;

    SecondaryProfile(Degree modulation, boolean major, Chord[] resolutions, Chord[] pivots) {
        this.modulation = modulation;
        this.major = major;
        this.resolutions = resolutions;
        this.pivots = pivots;
    }

    Chord tonicized() {
        return resolutions[0];
    }
}

