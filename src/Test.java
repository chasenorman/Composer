import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.function.BiPredicate;

public interface Test extends BiPredicate<State, State> {
    int[] LOWS = {40, 48, 53, 60};
    int[] HIGHS = {64, 72, 77, 84};
    int[] DISTANCES = {21, 12, 12};

    Test[] tests = new Test[]{Test::arraySizesAndNull, Test::overlap, Test::voiceDistances, Test::rangeTest, /*Test::spelling,*/
            Test::cadenceProperlyLabelled, /*Test::inversionProperlyLabelled,*/ Test::modeCheck, Test::inversionComplete, Test::modulatingOnV,
            Test::key, Test::retrogression, Test::dissonantInversionIsPassing, Test::passingBassStepwise, Test::beforePassingBassStepwise,
            Test::seventhTendencyTone, Test::modulationHasPivotOrPassing, Test::directFifthsAndOctaves, Test::parallelFifthsAndOctaves, Test::A2,
            Test::crossRelation, Test::hasRootAndThird, Test::tripleOnlyCadence, Test::doubledChromatic, Test::minorChromaticsInPassing,
            Test::doubledLeadingTone, Test::leadingTendencyTone, Test::N6InversionDoubling, Test::N6Tendencies, Test::A6InversionDoubling,
            Test::Fr6InversionDoubling, Test::A6Tendencies, Test::bassD5P5, Test::preparedSeventh, Test::fakeChords, Test::oddIntervals, Test::voiceDirection,
            Test::twoPassing, Test::majorSeventh};

    Test[] currentTests = new Test[]{
            Test::voiceDistances, Test::rangeTest, Test::modeCheck, Test::dissonantInversionIsPassing, Test::hasRootAndThird,
            Test::N6InversionDoubling, Test::tripleOnlyCadence, Test::minorChromaticsInPassing, Test::doubledLeadingTone, Test::A6InversionDoubling,
            Test::fakeChords, Test::Fr6InversionDoubling, Test::majorSeventh
    };

    String[] names = new String[]{"arraySizesAndNull", "overlap", "voiceDistances", "rangeTest", /*"spelling",*/
            "cadenceProperlyLabelled", "modeCheck", "inversionComplete", "modulatingOnV",
            "key", "retrogression", "dissonantInversionIsPassing", "passingBassStepwise", "beforePassingBassStepwise",
            "seventhTendencyTone", "modulationHasPivotOrPassing", "directFifthsAndOctaves", "parallelFifthsAndOctaves", "A2",
            "crossRelation", "hasRootAndThird", "tripleOnlyCadence", "doubledChromatic", "minorChromaticsInPassing",
            "doubledLeadingTone", "leadingTendencyTone", "N6InversionDoubling", "N6Tendencies", "A6InversionDoubling",
            "Fr6InversionDoubling", "A6Tendencies", "bassD5P5", "preparedSeventh", "fakeChords", "oddIntervals", "voiceDirection",
            "twoPassing", "majorSeventh"};

    static boolean all(State curr) {
        for (Test t : currentTests) {
            if (!t.test(null, curr)) {
                return false;
            }
        }
        return true;
    }

    static boolean all(State prev, State curr) {
        for (Test t : tests) {
            if (!t.test(prev, curr)) {
                return false;
            }
        }
        return true;
    }


    static boolean reasonsForPassing(State prev, State curr) {
        return dissonantInversionIsPassing(prev, curr) && modulationHasPivotOrPassing(prev, curr) && minorChromaticsInPassing(prev, curr)
                && retrogression(prev, curr);
    }

    static boolean reasonsForCadence(State prev, State curr) {
        return cadenceProperlyLabelled(prev, curr) && directFifthsAndOctaves(prev, curr) && parallelFifthsAndOctaves(prev, curr)
                && tripleOnlyCadence(prev, curr);
    }


    static boolean arraySizesAndNull(State prev, State curr) {
        return nullCheck(curr) && nullCheck(prev);
    }

    static boolean nullCheck(State curr) {
        if (curr == null) {
            return false;
        }
        if (curr.chord == null || curr.key == null ||
                curr.voicing == null || curr.degrees == null || curr.midi == null) {
            return false;
        }
        if (curr.modulating && curr.chord.profile == null) {
            return false;
        }
        if (curr.voicing.length != Voice.VOICES || curr.degrees.length != Voice.VOICES || curr.midi.length != Voice.VOICES) {
            return false;
        }
        for (int i = 0; i < Voice.VOICES; i++) {
            if (curr.voicing[i] == null || curr.degrees[i] == null) {
                return false;
            }
        }
        return true;
    }

    static boolean overlap(State prev, State curr) {
        for (int i = 0; i < Voice.VOICES-1; i++) {
            if (prev.midi[i] > curr.midi[i+1] || prev.midi[i+1] < curr.midi[i]) {
                return false;
            }
        }
        return true;
    }

    static boolean voiceDistances(State prev, State curr) {
        for (int i = 0; i < Voice.VOICES - 1; i++) {
            int distance = curr.midi[i+1] - curr.midi[i];
            if (distance > DISTANCES[i] || distance < 0) {
                return false;
            }
        }
        return true;
    }

    static boolean rangeTest(State prev, State curr) {
        for (int i = 0; i < Voice.VOICES; i++) {
            if (curr.midi[i] < LOWS[i] || curr.midi[i] > HIGHS[i]) {
                return false;
            }
        }
        return true;
    }

    /*static boolean spelling(State prev, State curr) {
        ChordTone[] v = curr.voicing;
        for (int voice = 0; voice < Voice.VOICES; voice++) {
            if (v[voice] == null) {
                return false;
            }
        }
        return true;
    }*/

    // false negative on V -> VI, V, V -> I, what inversions?
    // false positive on V -> I root
    static boolean cadenceProperlyLabelled(State prev, State curr) {
        if (!curr.cadence) {
            return (!prev.chord.equals(Chord.V) && !prev.chord.equals(Chord.V_MINOR)) ||
                    (!curr.chord.equals(Chord.I) && !curr.chord.equals(Chord.i)) ||
                    !prev.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT) || !curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT);
        }
        return isCadence(prev, curr);
    }

    static boolean isCadence(State prev, State curr) {
        return (((curr.chord.equals(Chord.V) || curr.chord.equals(Chord.V_MINOR)) && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT)) ||
                (prev.chord.type.equals(ChordType.DOMINANT) && curr.chord.type.equals(ChordType.TONIC) && curr.consonant()) ||
                ((prev.chord.type.equals(ChordType.SECONDARY_LEADING) || prev.chord.type.equals(ChordType.SECONDARY_DOMINANT))
                        && curr.chord.type.equals(ChordType.TONIC) && curr.consonant() && prev.modulating)) && !curr.seventh();
    }

    // check chords match mode as well.
    static boolean modeCheck(State prev, State curr) {
        return curr.chord.major == curr.key.major;
    }

    static boolean inversionComplete(State prev, State curr) {
        if (curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT)) {
            return true;
        }

        int[] counts = curr.counts();
        return counts[ChordTone.ROOT.ordinal()] > 0 && counts[ChordTone.THIRD.ordinal()] > 0 && counts[ChordTone.FIFTH.ordinal()] > 0;
    }

    static boolean modulatingOnV(State prev, State curr) {
        return !curr.modulating || curr.chord.type.equals(ChordType.SECONDARY_DOMINANT) || curr.chord.type.equals(ChordType.SECONDARY_LEADING);
    }

    // TODO cadence?
    static boolean key(State prev, State curr) {
        return prev.modulating ? curr.key.equals(prev.key.underModulation(prev.chord.profile)) : prev.key.equals(curr.key);
    }

    // PD -x> T
    // D -x> PD
    // V/ -> I/ or VI/
    // vii/ -> I/ or VI/
    // TODO passing?
    static boolean retrogression(State prev, State curr) {
        boolean one = curr.chord.equals(Chord.I) || curr.chord.equals(Chord.i);
        if (prev.cadence) {
            return one;
        }
        else if (prev.chord.type.equals(ChordType.PREDOMINANT) && one) {
            return curr.passing;
        }
        else if (prev.chord.type.equals(ChordType.DOMINANT) && curr.chord.type.equals(ChordType.PREDOMINANT)) {
            return false;
        }
        else if (prev.modulating) {
            return curr.chord.equals(Chord.i) || curr.chord.equals(Chord.I);
        }
        else if (prev.chord.type.equals(ChordType.SECONDARY_LEADING) || prev.chord.type.equals(ChordType.SECONDARY_DOMINANT)) {
            if ((prev.chord.profile.equals(SecondaryProfile.V) || prev.chord.profile.equals(SecondaryProfile.v))
                    && one &&
                    curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH)) {
                return true;
            }
            if (prev.chord.profile.equals(curr.chord.profile)) {
                return true;
            }
            for (Chord resolution : prev.chord.profile.resolutions) {
                if (curr.chord.equals(resolution)) {
                    return true;
                }
            }
            return false;
        }
        else {
            boolean five = curr.chord.equals(Chord.V) || curr.chord.equals(Chord.V_MINOR);
            if (prev.chord.equals(Chord.A6) || prev.chord.equals(Chord.Fr6)) {
                return (five && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT)) ||
                        (one && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH));
            }
            else if (prev.chord.equals(Chord.N6)) {
                return (five && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT)) ||
                        (one && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH)) ||
                        (SecondaryProfile.v.equals(curr.chord.profile));
            }
            else if ((prev.chord.equals(Chord.I) || prev.chord.equals(Chord.i)) && prev.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH)) {
                return five && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.ROOT);
            }
        }
        return true;
    }

    static boolean dissonantInversionIsPassing(State prev, State curr) {
        if (curr.chord == Chord.Fr6) {
            return true;
        }
        return curr.passing || curr.consonant();
    }

    static boolean passingBassStepwise(State prev, State curr) {
        int bass = prev.intervals(curr)[Voice.BASS.ordinal()].number;
        return !prev.passing || (bass <= 1 && bass >= -1);
    }

    static boolean beforePassingBassStepwise(State prev, State curr) {
        int bass = prev.intervals(curr)[Voice.BASS.ordinal()].number;
        return !curr.passing || (bass <= 1 && bass >= -1);
    }

    static boolean seventhTendencyTone(State prev, State curr) {
        if (!prev.seventh()) {
            return true;
        }
        Interval[] intervals = prev.intervals(curr);
        for (int i = 0; i < Voice.VOICES; i++) {
            if (prev.voicing[i].equals(ChordTone.SEVENTH) && (prev.midi[i] < curr.midi[i] || !intervals[i].stepwise())) {
                return false;
            }
        }
        return true;
    }

    static boolean modulationHasPivotOrPassing(State prev, State curr) {
        if (!curr.modulating || curr.passing) {
            return true;
        }
        for (Chord pivot : curr.chord.profile.pivots) {
            if (prev.chord.equals(pivot)) {
                return true;
            }
        }
        return false;
    }

    // unless cadence
    // TODO only between bass and soprano?
    static boolean directFifthsAndOctaves(State prev, State curr) {
        Interval[] intervals = prev.intervals(curr);
        if (prev.cadence || intervals[Voice.SOPRANO.ordinal()].stepwise()) {
            return true;
        }
        for (int v = 0; v < Voice.VOICES; v++) {
            if (v == Voice.SOPRANO.ordinal()) {
                continue;
            }
            Interval i = curr.degrees[v].to(curr.degrees[Voice.SOPRANO.ordinal()]).octaveMod();
            if ((i.equals(Interval.PERFECT_UNISON) || i.equals(Interval.PERFECT_FIFTH))
                    && ((curr.midi[v] - prev.midi[v]) *
                    (curr.midi[Voice.SOPRANO.ordinal()] - prev.midi[Voice.SOPRANO.ordinal()])) > 0) {
                return false;
            }
        }
        return true;
    }

    // unless cadence
    static boolean parallelFifthsAndOctaves(State prev, State curr) {
        if (prev.cadence) {
            return true;
        }
        Interval[] intervals = prev.intervals(curr);
        for (int i = 0; i < Voice.VOICES - 1; i++) {
            for (int j = i+1; j < Voice.VOICES; j++) {
                if (prev.voiceInterval(i, j).octaveMod().equals(Interval.PERFECT_FIFTH)
                        && curr.voiceInterval(i, j).octaveMod().equals(Interval.PERFECT_FIFTH)
                        && !intervals[i].equals(Interval.PERFECT_UNISON)) {
                    return false;
                }
                if (prev.voiceInterval(i, j).octaveMod().equals(Interval.PERFECT_UNISON)
                        && curr.voiceInterval(i, j).octaveMod().equals(Interval.PERFECT_UNISON)
                        && !intervals[i].equals(Interval.PERFECT_UNISON)) {
                    return false;
                }
            }
        }
        return true;
    }

    // maybe sequence violation
    static boolean A2(State prev, State curr) {
        Interval[] intervals = prev.intervals(curr);
        for (int i = 0; i < Voice.VOICES; i++) {
            if (intervals[i].octaveMod().equals(Interval.AUGMENTED_SECOND) || intervals[i].octaveMod().equals(Interval.DIMINISHED_SEVENTH)) {
                return false;
            }
        }
        return true;
    }

    static boolean crossRelation(State prev, State curr) {
        if (prev.cadence || prev.chord == Chord.N6) {
            return true;
        }
        for(int i = 0; i < Voice.VOICES; i++) {
            for (int j = 0; j < Voice.VOICES; j++) {
                if (i != j && prev.degrees[i].intervalFromRoot.number == curr.degrees[j].intervalFromRoot.number &&
                        prev.degrees[i].intervalFromRoot.semitones != curr.degrees[j].intervalFromRoot.semitones &&
                        !prev.degrees[j].equals(prev.degrees[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean hasRootAndThird(State prev, State curr) {
        int[] counts = curr.counts();
        return counts[ChordTone.ROOT.ordinal()] > 0 && counts[ChordTone.THIRD.ordinal()] > 0;
    }

    // also no doubled seventh?
    static boolean tripleOnlyCadence(State prev, State curr) {
        int[] counts = curr.counts();
        if (counts[ChordTone.ROOT.ordinal()] >= 3 && !curr.cadence) {
            return false;
        }
        if (counts[ChordTone.THIRD.ordinal()] >= 3) {
            return false;
        }
        return counts[ChordTone.SEVENTH.ordinal()] < 2;
    }

    static boolean doubledChromatic(State prev, State curr) {
        for (int i = 0; i < Voice.VOICES; i++) {
            for (int j = i+1; j < Voice.VOICES; j++) {
                if (curr.degrees[i].equals(curr.degrees[j]) && !curr.degrees[i].diatonic(curr.key.major)) {
                    return false;
                }
            }
        }
        return true;
    }

    // TODO is this true?
    static boolean minorChromaticsInPassing(State prev, State curr) {
        if (curr.passing) {
            return true;
        }
        return !curr.chord.equals(Chord.v) && !curr.chord.equals(Chord.vidim) && !curr.chord.equals(Chord.IV_MINOR);
    }

    // third of V or root of VII, secondaries
    static boolean doubledLeadingTone(State prev, State curr) {
        int leadingTones = 0;
        for (Degree d : curr.degrees) {
            if (d.equals(Degree._7)) {
                leadingTones++;
            }
        }
        int[] counts = curr.counts();
        switch(curr.chord.type) {
            case SECONDARY_LEADING: return counts[ChordTone.ROOT.ordinal()] < 2;
            case SECONDARY_DOMINANT: return counts[ChordTone.THIRD.ordinal()] < 2;
            default: return leadingTones <= 1;
        }
    }

    // third of V or root of VII, secondaries
    // there was an exception
    // TODO does frustrated leading tone apply to secondaries? In an inner voice it's ok. Would prefer incomplete chord.
    static boolean leadingTendencyTone(State prev, State curr) {
        Interval[] intervals = prev.intervals(curr);

        if (prev.chord.equals(Chord.V) || prev.chord.equals(Chord.V_MINOR) || prev.chord.type.equals(ChordType.SECONDARY_DOMINANT)) {
            for (Voice v : Voice.OUTER) {
                if (prev.voicing[v.ordinal()].equals(ChordTone.THIRD) && !intervals[v.ordinal()].equals(Interval.MINOR_SECOND)) {
                    return prev.chord.profile != null && prev.chord.profile.equals(curr.chord.profile);
                }
            }
        } else if (prev.chord.equals(Chord.viidim) || prev.chord.equals(Chord.viihalfdim) || prev.chord.type.equals(ChordType.SECONDARY_LEADING)) {
            for (Voice v : Voice.OUTER) {
                if (prev.voicing[v.ordinal()].equals(ChordTone.ROOT) && !intervals[v.ordinal()].equals(Interval.MINOR_SECOND)) {
                    return prev.chord.profile != null && prev.chord.profile.equals(curr.chord.profile);
                }
            }
        } else if (prev.chord.profile == null) { // TODO based on what he said in class
            for (Voice v : Voice.OUTER) {
                if (prev.degrees[v.ordinal()].equals(Degree._7) && !intervals[v.ordinal()].equals(Interval.MINOR_SECOND)) {
                    return false;
                }
            }
        }

        return true;
    }

    static boolean N6InversionDoubling(State prev, State curr) {
        return !curr.chord.equals(Chord.N6) || curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.THIRD);
    }

    static boolean N6Tendencies(State prev, State curr) {
        if (!curr.chord.equals(Chord.N6)) {
            return true;
        }
        for (int i = 0; i < Voice.VOICES; i++) {
            if (prev.degrees[i].equals(Degree.b2) && curr.degrees[i].equals(Degree._2)) {
                return false;
            }
        }
        return true;
    }


    static boolean A6InversionDoubling(State prev, State curr) {
        if (!curr.chord.equals(Chord.A6)) {
            return true;
        }
        return curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.THIRD);
    }

    static boolean Fr6InversionDoubling(State prev, State curr) {
        if (!curr.chord.equals(Chord.Fr6)) {
            return true;
        }
        return curr.seventh() && curr.voicing[Voice.BASS.ordinal()].equals(ChordTone.FIFTH);
    }

    // A6 and Fr6
    static boolean A6Tendencies(State prev, State curr) {
        if (!curr.chord.equals(Chord.Fr6) && !curr.chord.equals(Chord.A6)) {
            return true;
        }
        for (int i = 0; i < Voice.VOICES; i++) {
            if ((prev.degrees[i].equals(Degree.b6) || prev.degrees[i].equals(Degree.$4)) && !curr.degrees[i].equals(Degree._5)) {
                return false;
            }
        }
        return true;
    }

    // m6? M6?
    static boolean bassD5P5(State prev, State curr) {
        for (int i = 0; i < Voice.VOICES; i++) {
            if (prev.voiceInterval(Voice.BASS.ordinal(), i).octaveMod().equals(Interval.DIMINISHED_FIFTH)
                    && curr.voiceInterval(Voice.BASS.ordinal(), i).octaveMod().equals(Interval.PERFECT_FIFTH)) {
                return false;
            }
        }
        return true;
    }

    // TODO "nondominant sevenths must be prepared" - what about dominant sevenths?
    // maybe not N6 A6
    static boolean preparedSeventh(State prev, State curr) {
        if (!curr.seventh()) {
            return true;
        }
        Interval[] intervals = prev.intervals(curr);
        for (int i = 0; i < Voice.VOICES; i++) {
            if (curr.voicing[i].equals(ChordTone.SEVENTH) && (intervals[i].number < -1 || intervals[i].number > 1)) {
                return false;
            }
        }
        return true;
    }

    // A list of chords that are stupid according to our instructor.
    static boolean fakeChords(State prev, State curr) {
        if (curr.chord.equals(Chord.iii) || curr.chord.equals(Chord.vi) || curr.chord.equals(Chord.IV)) {
            return curr.consonant() || !curr.seventh();
        }
        return true;
    }

    static boolean oddIntervals(State prev, State curr) {
        for (Interval i : prev.intervals(curr)) {
            if (i == null) {
                return false;
            }
            Quality quality = i.quality();
            if (quality.uncommon()) {
                return false;
            }
            if ((i.number > 4 || i.number < -4) && (quality.equals(Quality.AUGMENTED) || quality.equals(Quality.DIMINISHED))) {
                return false;
            }
            if (i.number > 9 || i.number < -9) {
                return false;
            }
        }
        return true;
    }

    static boolean voiceDirection(State prev, State curr) {
        if (prev.cadence) {
            return true;
        }
        Interval[] intervals = prev.intervals(curr);
        int direction = 0;
        for (Interval i : intervals) {
            if (i.equals(Interval.PERFECT_UNISON)) {
                return true;
            }
            if (i.semitones > 0) {
                if (direction == -1) {
                    return true;
                }
                direction = 1;
            }
            if (i.semitones < 0) {
                if (direction == 1) {
                    return true;
                }
                direction = -1;
            }
        }
        return false;
    }

    // TODO is this real?
    static boolean majorSeventh(State prev, State curr) {
        return (curr.chord != Chord.I && curr.chord != Chord.i && curr.chord != Chord.IV && curr.chord != Chord.iv
                && curr.chord != Chord.III && curr.chord != Chord.VI)
                || !curr.seventh();
    }

    static boolean twoPassing(State prev, State curr) {
        return !prev.passing || !curr.passing;
    }
}
