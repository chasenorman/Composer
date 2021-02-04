import java.util.function.BiFunction;

public interface Preference extends BiFunction<State, State, Float> {

    static Preference[] preferences = {};

    static float functionChange(State prev, State curr) {
        if (prev.chord.profile != null) {
            return prev.chord.profile == curr.chord.profile ? 0 : 1;
        }
        return prev.chord == curr.chord ? 0 : 1;
    }

    static float diatonicMelody(State prev, State curr) {
        return curr.degrees[Voice.SOPRANO.ordinal()].diatonic(curr.key.major) ? 1 : 0;
    }

    static float stepwiseMotion(State prev, State curr) {
        float total = 0;
        for (Voice v : Voice.values()) {
            if (prev.degrees[v.ordinal()].to(curr.degrees[v.ordinal()]).stepwise()) {
                total += (1. / Voice.VOICES);
            }
        }
        return total;
    }

    static float nondeceptive(State prev, State curr) {
        if (prev.chord.type == ChordType.DOMINANT && curr.chord != Chord.I && curr.chord != Chord.i) {
            return 0;
        }
        if (prev.chord.profile == null || curr.chord.profile != null) {
            return 1;
        }
        return curr.chord.equals(prev.chord.profile.tonicized()) ? 1 : 0;
    }

    static float nonpassing(State prev, State curr) {
        return curr.passing ? 0 : 1;
    }

    static float chordCommonality(State prev, State curr) {
        if (curr.chord.equals(Chord.iii)) {
            return 0;
        }
        if (curr.chord.profile != null) {
            return 0.5f;
        }
        if (curr.chord.equals(Chord.III) || curr.chord.equals(Chord.VII)) {
            return 0.5f;
        }
        if (curr.chord.equals(Chord.vi) || curr.chord.equals(Chord.VI)) {
            return 0.75f;
        }
        return 1;
    }

    static float doubling(State prev, State curr) {
        if(curr.chord.equals(Chord.N6) || curr.chord.equals(Chord.A6) || curr.chord.equals(Chord.Fr6)) {
            return 1;
        }
        int[] counts = curr.counts();
        if (counts[0] == 2 || counts[curr.voicing[Voice.BASS.ordinal()].ordinal()] == 2) {
            return 1;
        }
        if (counts[1] == 2) {
            return 0.5f;
        }
        return 0;
    }

    static float diminishedInversion(State prev, State curr) {
        if ()
    }
}
