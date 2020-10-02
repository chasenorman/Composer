import java.util.*;

public class State {
    public static State context;

    public static final String[] INVERSIONS = {"", "6", "64", null};
    public static final String[] SEVENTH_INVERSIONS = {"7", "65", "43", "42"};

    public final Chord chord;
    public final Key key;

    public boolean cadence;
    public final boolean modulating;
    public boolean passing;

    public int[] midi;
    public ChordTone[] voicing;
    public Degree[] degrees;

    // boolean strong?

    public State(Note soprano, Note alto, Note tenor, Note bass, Chord chord, Key key, boolean cadence, boolean modulating, boolean passing) {
        this.chord = chord;
        this.key = key;
        this.cadence = cadence;
        this.modulating = modulating;
        this.passing = passing;
        this.midi = new int[]{bass.midi, tenor.midi, alto.midi, soprano.midi};
        this.voicing = new ChordTone[]{bass.voice, tenor.voice, alto.voice, soprano.voice};
        this.degrees = new Degree[]{bass.degree, tenor.degree, alto.degree, soprano.degree};
    }

    public State(int soprano, int alto, int tenor, int bass, Chord chord, Key key, boolean cadence, boolean modulating, boolean passing) {
        this.midi = new int[]{bass, tenor, alto, soprano};
        this.chord = chord;
        this.key = key;
        this.cadence = cadence;
        this.modulating = modulating;
        this.passing = passing;
        setVoicing();
        setDegrees();
        context = this;
    }

    public State(int soprano, int alto, int tenor, int bass, Key key, boolean cadence, boolean modulating, boolean passing) {
        this.midi = new int[]{bass, tenor, alto, soprano};
        Chord result = null;
        Chord: for (Chord c : Chord.values()) {
            if (c.major != key.major || (modulating && c.profile == null)) {
                continue;
            }
            int[] tones = new int[]{key.offset(c.root), key.offset(c.third), key.offset(c.fifth), key.offset(c.seventh)};
            boolean[] has = new boolean[ChordTone.TONES];
            Midi: for (int m : midi) {
                for(int i = 0; i < ChordTone.TONES; i++) {
                    if (tones[i] == (m % Key.MAX_OFFSET)) {
                        has[i] = true;
                        continue Midi;
                    }
                }
                continue Chord;
            }
            if (!has[ChordTone.ROOT.ordinal()] || !has[ChordTone.THIRD.ordinal()]) {
                continue;
            }
            result = c;
            break;
        }
        this.chord = result;
        this.key = key;
        this.cadence = cadence;
        this.modulating = modulating;
        this.passing = passing;
        setVoicing();
        setDegrees();
        context = this;
    }

    public State(int soprano, int alto, int tenor, int bass, State prev, boolean cadence, boolean modulating, boolean passing) {
        this(soprano, alto, tenor, bass,
                (prev == null || prev.cadence) ? detectKey(soprano, alto, tenor, bass) : prev.nextKey(), cadence, modulating, passing);
    }

    private static Key detectKey(int soprano, int alto, int tenor, int bass) {
        int[] midi = {bass, tenor, alto, soprano};
        Key: for (Key k : Key.DEFAULTS) {
            Chord c = k.major ? Chord.I : Chord.i;
            int[] tones = new int[]{k.offset(c.root), k.offset(c.third), k.offset(c.fifth), k.offset(c.seventh)};
            boolean[] has = new boolean[ChordTone.TONES];
            Midi: for (int m : midi) {
                for(int i = 0; i < ChordTone.TONES; i++) {
                    if (tones[i] == (m % Key.MAX_OFFSET)) {
                        has[i] = true;
                        continue Midi;
                    }
                }
                continue Key;
            }
            if (!has[ChordTone.ROOT.ordinal()] || !has[ChordTone.THIRD.ordinal()] || has[ChordTone.SEVENTH.ordinal()]) {
                continue;
            }
            return k;
        }
        return null;
    }

    public State(int soprano, int alto, int tenor, int bass, State prev, boolean cadence, boolean modulating) {
        this(soprano, alto, tenor, bass, prev, cadence, modulating, false);
        if (prev != null && !prev.cadence && !prev.passing && !Test.reasonsForPassing(prev, this)) {
            passing = true;
        }
    }

    public State(int soprano, int alto, int tenor, int bass, State prev, boolean modulating) {
        this(soprano, alto, tenor, bass, prev, false, modulating);
        if (prev != null && !prev.cadence && Test.isCadence(prev, this) && !Test.reasonsForCadence(prev, this)) {
            cadence = true;
        }
    }

    public State(int soprano, int alto, int tenor, int bass, State prev) {
        this(soprano, alto, tenor, bass, prev, false);
    }

    public State(int soprano, int alto, int tenor, int bass) {
        this(soprano, alto, tenor, bass, context);
    }

    public boolean consonant() {
        return voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT) || voicing[Voice.BASS.ordinal()].equals(ChordTone.THIRD);
    }

    public int[] counts() {
        int[] counts = new int[ChordTone.TONES];
        for (ChordTone c : voicing) {
            if (c != null) {
                counts[c.ordinal()]++;
            }
        }
        return counts;
    }

    public boolean seventh() {
        for (ChordTone c : voicing) {
            if (c == ChordTone.SEVENTH) {
                return true;
            }
        }
        return false;
    }

    public String inversion() {
        return seventh() ? SEVENTH_INVERSIONS[voicing[Voice.BASS.ordinal()].ordinal()] :
                INVERSIONS[voicing[Voice.BASS.ordinal()].ordinal()];
    }


    public String toString() {
        if (modulating) {
            String str = chord.toString();
            str = str.substring(0, str.indexOf('/'));
            str += inversion();
            if (!seventh()) {
                str = str.replace('ø','o');
            }
            return key.underModulation(chord.profile) + ": " + str;
        }

        if (chord.equals(Chord.A6)) {
            return seventh() ? "Ger6" : "It6";
        }

        String str = (passing ? "[" : "") + chord;

        if (!chord.equals(Chord.N6) && !chord.equals(Chord.Fr6)) {
            int i = str.indexOf('/');
            if (i != -1) {
                str = str.replaceFirst("/", inversion() + "/");
            } else {
                str += inversion();
            }
        }

        if (!seventh()) {
            str = str.replace('ø','o');
        }

        return str + (passing ? "]" : "");
    }

    private void setVoicing() {
        ChordTone[] result = new ChordTone[Voice.VOICES];
        int[] tones = new int[]{key.offset(chord.root), key.offset(chord.third), key.offset(chord.fifth), key.offset(chord.seventh)};
        Outer: for (int voice = 0; voice < Voice.VOICES; voice++) {
            for (int tone = 0; tone < ChordTone.TONES; tone++) {
                if (midi[voice] % Key.MAX_OFFSET == tones[tone]) {
                    result[voice] = ChordTone.ORDER[tone];
                    continue Outer;
                }
            }
        }
        this.voicing = result;
    }

    private void setDegrees() {
        Degree[] degrees = new Degree[voicing.length];
        for (int i = 0; i < voicing.length; i++) {
            if (voicing[i] != null) {
                degrees[i] = chord.notes[voicing[i].ordinal()];
            }
        }
        this.degrees = degrees;
    }

    public Interval[] intervals(State o) {
        Interval[] result = new Interval[Voice.VOICES];
        for (int i = 0; i < Voice.VOICES; i++) {
            result[i] = fromMidi0(o.midi[i], o.key, o.degrees[i]).sub(fromMidi0(midi[i], key, degrees[i]));
        }
        return result;
    }

    private static Interval fromMidi0(int midi, Key key, Degree degree) {
        Interval offset = key.intervalFromC.add(degree.intervalFromRoot).octaveMod();
        return new Interval(offset.number + Key.MAX_NUMBER*((midi - offset.semitones)/Key.MAX_OFFSET), midi);
    }

    public Interval voiceInterval(int v1, int v2) {
        return fromMidi0(midi[v2], key, degrees[v2]).sub(fromMidi0(midi[v1], key, degrees[v1]));
    }

    public boolean equals(Object o) {
        if (!(o instanceof State)) {
            return false;
        }
        State s = (State)o;

        return Objects.equals(chord, s.chord) && Objects.equals(key, s.key) &&
                cadence == s.cadence && modulating == s.modulating && passing == s.passing &&
                Arrays.equals(midi, s.midi) && Arrays.equals(voicing, s.voicing) && Arrays.equals(degrees, s.degrees);
    }

    public Key nextKey() {
        return modulating ? key.underModulation(chord.profile) : key;
    }

    public static State[] piece(int[]... midi) {
        State[] result = new State[midi.length];
        result[0] = new State(midi[0][0], midi[0][1], midi[0][2], midi[0][3], null);
        for (int i = 1; i < midi.length; i++) {
            result[i] = new State(midi[i][0], midi[i][1], midi[i][2], midi[i][3], result[i-1]);
        }
        //if (midi.length > 1) {
        //    result[midi.length - 1] = new State(midi[midi.length - 1][0], midi[midi.length - 1][1], midi[midi.length - 1][2], midi[midi.length - 1][3],
        //            result[midi.length - 2], true,);
        //}
        return result;
    }

    public static State[] piece(int[] soprano, int[] alto, int[] tenor, int[] bass) {
        State[] result = new State[soprano.length];
        result[0] = new State(soprano[0], alto[0], tenor[0], bass[0], null);
        for (int i = 1; i < soprano.length; i++) {
            result[i] = new State(soprano[i], alto[i], tenor[i], bass[i], result[i-1]);
        }
        //if (midi.length > 1) {
        //    result[midi.length - 1] = new State(midi[midi.length - 1][0], midi[midi.length - 1][1], midi[midi.length - 1][2], midi[midi.length - 1][3],
        //            result[midi.length - 2], true,);
        //}
        return result;
    }


    public List<Chord> chords() {
        List<Chord> chords = new ArrayList<Chord>();
        if (cadence || modulating) {
            chords = nextKey().major ? Collections.singletonList(Chord.I) : Collections.singletonList(Chord.i);
        } else {
            for (Chord c : Chord.values()) {
                if (nextKey().major == c.major && possible(c)) {
                    chords.add(c);
                }
            }
        }
        return chords;
    }

    static class Note {
        int midi;
        ChordTone voice;
        Degree degree;

        Note(int midi, ChordTone voice, Degree degree) {
            this.midi = midi;
            this.voice = voice;
            this.degree = degree;
        }
    }

    public List<State> next() {
        List<Chord> chords = chords();
        Key key = nextKey();

        List<State> states = new ArrayList<>();

        for (Chord c : chords) {
            List<Note> soprano = new ArrayList<>();
            List<Note> alto = new ArrayList<>();
            List<Note> tenor = new ArrayList<>();
            List<Note> bass = new ArrayList<>();

            for (int octave = 3; octave < 8; octave++) {
                for (int tone = 0; tone < ChordTone.TONES; tone++) {
                    Note n = new Note(key.offset(c.notes[tone]) + Key.MAX_OFFSET*octave, ChordTone.values()[tone], c.notes[tone]);
                    if (canFollow(key, Voice.SOPRANO, n)) {
                        soprano.add(n);
                    }
                    if (canFollow(key, Voice.ALTO, n)) {
                        alto.add(n);
                    }
                    if (canFollow(key, Voice.TENOR, n)) {
                        tenor.add(n);
                    }
                    if (canFollow(key, Voice.BASS, n)) {
                        bass.add(n);
                    }
                }
            }

            for (Note s : soprano) {
                for (Note a : alto) {
                    for (Note t : tenor) {
                        for(Note b : bass) {

                            State state = new State(s, a, t, b, c, key, false, false, false);
                            if (Test.all(this, state)) {
                                states.add(state);
                            }

                            boolean isPassing = !cadence && !passing;
                            boolean isCadence = !cadence && Test.isCadence(this, state);

                            if (isPassing) {
                                State state2 = new State(s, a, t, b, c, key, false, false, true);
                                if (Test.all(this, state2)) {
                                    states.add(state2);
                                }
                            }

                            if (isCadence) {
                                State state2 = new State(s, a, t, b, c, key, true, false, false);
                                if (Test.all(this, state2)) {
                                    states.add(state2);
                                }
                            }

                            if (isPassing && isCadence) {
                                State state2 = new State(s, a, t, b, c, key, true, false, true);
                                if (Test.all(this, state2)) {
                                    states.add(state2);
                                }
                            }

                            if (c.profile != null) {
                                State state2 = new State(s, a, t, b, c, key, false, true, false);
                                if (Test.all(this, state2)) {
                                    states.add(state2);
                                }

                                if (isPassing) {
                                    State state3 = new State(s, a, t, b, c, key, false, true, true);
                                    if (Test.all(this, state3)) {
                                        states.add(state3);
                                    }
                                }

                                if (isCadence) {
                                    State state3 = new State(s, a, t, b, c, key, true, true, false);
                                    if (Test.all(this, state3)) {
                                        states.add(state3);
                                    }
                                }

                                if (isPassing && isCadence) {
                                    State state3 = new State(s, a, t, b, c, key, true, true, true);
                                    if (Test.all(this, state3)) {
                                        states.add(state3);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        return states;
    }

    private boolean canFollow(Key k, Voice v, Note n) {
        if (n.midi < Test.LOWS[v.ordinal()] || n.midi > Test.HIGHS[v.ordinal()]) {
            return false;
        }
        return true; //TODO improve
    }

    private boolean possible(Chord c) {
        if (this.chord.type.equals(ChordType.PREDOMINANT) && (c.equals(Chord.I) || c.equals(Chord.i))) {
            return !this.passing;
        }
        else if (this.chord.type.equals(ChordType.DOMINANT) && c.type.equals(ChordType.PREDOMINANT)) {
            return false;
        }
        else if (this.chord.type.equals(ChordType.SECONDARY_LEADING) || this.chord.type.equals(ChordType.SECONDARY_DOMINANT)) {
            if ((this.chord.profile.equals(SecondaryProfile.V) || this.chord.profile.equals(SecondaryProfile.v))
                    && (c.equals(Chord.I) || c.equals(Chord.i))) {
                return true;
            }
            if (this.chord.profile.equals(c.profile)) {
                return true;
            }
            for (Chord resolution : this.chord.profile.resolutions) {
                if (c.equals(resolution)) {
                    return true;
                }
            }
            return false;
        }
        else if (this.chord.equals(Chord.N6) || this.chord.equals(Chord.A6) || this.chord.equals(Chord.Fr6)) {
            return (c.equals(Chord.V) || c.equals(Chord.V_MINOR)) ||
                    (c.equals(Chord.I) || c.equals(Chord.i));
        }
        else if ((this.chord.equals(Chord.I) || this.chord.equals(Chord.i)) && this.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH)) {
            return (c.equals(Chord.V) || c.equals(Chord.V_MINOR));
        }
        return true;
    }


    public static List<State> startingChords() {
        List<State> states = new ArrayList<>();
        for (Key key : Key.DEFAULTS) {
            Chord c = key.major ? Chord.I : Chord.i;
            List<Note> soprano = new ArrayList<>();
            List<Note> alto = new ArrayList<>();
            List<Note> tenor = new ArrayList<>();
            List<Note> bass = new ArrayList<>();

            for (int octave = 3; octave < 8; octave++) {
                for (int tone = 0; tone < ChordTone.TONES-1; tone++) {
                    Note n = new Note(key.offset(c.notes[tone]) + Key.MAX_OFFSET*octave, ChordTone.values()[tone], c.notes[tone]);
                    if (Voice.SOPRANO.withinRange(n.midi)) {
                        soprano.add(n);
                    }
                    if (Voice.ALTO.withinRange(n.midi)) {
                        alto.add(n);
                    }
                    if (Voice.TENOR.withinRange(n.midi)) {
                        tenor.add(n);
                    }
                    if (Voice.BASS.withinRange(n.midi)) {
                        bass.add(n);
                    }
                }
            }

            for (Note s : soprano) {
                for (Note a : alto) {
                    for (Note t : tenor) {
                        for(Note b : bass) {
                            State state = new State(s, a, t, b, c, key, false, false, false);
                            if (Test.all(state)) {
                                states.add(state);
                            }
                        }
                    }
                }
            }
        }
        return states;
    }
}
