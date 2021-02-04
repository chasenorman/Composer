import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class HarmonyCommand {
    public static final int LIMIT = 20;

    public static List<State> state = new ArrayList<>();
    public static List<State> displayed = null;

    @Command(name = "append", mixinStandardHelpOptions = true, version = "append 1.0",
            description = "Appends a new chord")
    public static class Append implements Callable<Integer> {
        @Parameters(index = "0", description = "The index of the state to add")
        int index;

        @Override
        public Integer call() throws Exception {
            if (displayed == null) {
                System.err.println("Must call \"find\" before an addition");
                return 1;
            }
            if (index < 0 || index > displayed.size()) {
                System.err.println("index out of bounds");
                return 1;
            }
            state.add(displayed.get(index));
            displayed = null;
            return 0;
        }
    }

    @Command(name = "find", mixinStandardHelpOptions = true, version = "find 1.0",
            description = "Finds a new chord")
    public static class Find implements Callable<Integer> {

        @Option(names = {"-k", "--key"}, description = "filter by key")
        String key;

        @Option(names = {"-c", "--chord"}, description = "filter by chord")
        String chord;

        @Option(names = {"-i", "--inversion"}, description = "filter by inversion")
        String inversion;

        @Option(names = {"-f", "--function"}, description = "filter by chord function")
        String function;

        @Option(names = { "-s", "--soprano" }, description = "soprano note")
        String soprano;

        @Option(names = { "-a", "--alto" }, description = "alto note")
        String alto;

        @Option(names = { "-t", "--tenor" }, description = "tenor note")
        String tenor;

        @Option(names = { "-b", "--bass" }, description = "bass note")
        String bass;

        @Override
        public Integer call() throws Exception {
            if (state.size() == 0) {
                displayed = State.startingChords();
            } else {
                displayed = state.get(state.size() - 1).next();
            }

            if (key != null) {
                key = key.replace("#", Accidental.SHARP_STRING);
                key = key.charAt(0) + key.substring(1).replace("b", Accidental.FLAT_STRING);
                displayed.removeIf(s -> !s.key.toString().equals(key));
            }
            if (chord != null) {
                displayed.removeIf(s -> !s.chord.str.replace('ø','o').equals(chord));
            }
            if (inversion != null) {
                displayed.removeIf(s -> !s.inversion().equals(inversion.replace("root", "")));
            }
            if (function != null && !function.isEmpty()) {
                displayed.removeIf(s -> !(s.chord.type.str.charAt(0) == function.charAt(0)));
            }
            if (soprano != null) {
                soprano = soprano.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.SOPRANO.ordinal()]
                        .noteString(s.key).equals(soprano));
            }
            if (alto != null) {
                alto = alto.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.ALTO.ordinal()]
                        .noteString(s.key).equals(alto));
            }
            if (tenor != null) {
                tenor = tenor.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.TENOR.ordinal()]
                        .noteString(s.key).equals(tenor));
            }
            if (bass != null) {
                bass = bass.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.BASS.ordinal()]
                        .noteString(s.key).equals(bass));
            }

            System.out.println(displayed.size() + " results found.");
            for (int i = 0; i < Math.min(displayed.size(), LIMIT); i++) {
                System.out.println("#" + i + ": " + displayed.get(i).detailedToString());
            }
            if (displayed.size() > LIMIT) {
                System.out.println("...");
            }
            if (displayed.size() == 0) {
                System.out.println("no chords found.");
            }
            return 0;
        }
    }

    @Command(name = "play", mixinStandardHelpOptions = true, version = "play 1.0",
            description = "Plays the song")
    public static class Play implements Callable<Integer> {
        @Option(names = { "-s", "--start" }, description = "the index to start", defaultValue = "0")
        int start;

        @Option(names = { "-e", "--end" }, description = "the index to end", defaultValue = "-1")
        int end;

        @Option(names = { "-f", "--find" }, description = "index of found value", defaultValue = "-1")
        int found;

        @Override
        public Integer call() throws Exception {
            if (found != -1) {
                Player.play(displayed.get(found), 400);
                return 0;
            }

            Player.play(state.subList(start, end == -1 ? state.size() : end).toArray(new State[0]));
            return 0;
        }
    }

    @Command(name = "undo", mixinStandardHelpOptions = true, version = "undo 1.0",
            description = "Removes the last state")
    public static class Undo implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            if (state == null || state.size() == 0) {
                System.err.println("Nothing to undo.");
                return 1;
            }
            state.remove(state.size() - 1);
            return 0;
        }
    }

    @Command(name = "list", mixinStandardHelpOptions = true, version = "list 1.0",
            description = "Lists all possible next chords and their count")
    public static class ListChords implements Callable<Integer> {
        @Option(names = {"-k", "--key"}, description = "filter by key")
        String key;

        @Option(names = {"-c", "--chord"}, description = "filter by chord")
        String chord;

        @Option(names = {"-i", "--inversion"}, description = "filter by inversion")
        String inversion;

        @Option(names = {"-f", "--function"}, description = "filter by chord function")
        String function;

        @Option(names = { "-s", "--soprano" }, description = "soprano note")
        String soprano;

        @Option(names = { "-a", "--alto" }, description = "alto note")
        String alto;

        @Option(names = { "-t", "--tenor" }, description = "tenor note")
        String tenor;

        @Option(names = { "-b", "--bass" }, description = "bass note")
        String bass;

        @Override
        public Integer call() throws Exception {
            if (state.size() == 0) {
                displayed = State.startingChords();
            } else {
                displayed = state.get(state.size() - 1).next();
            }

            if (key != null) {
                key = key.replace("#", Accidental.SHARP_STRING);
                key = key.charAt(0) + key.substring(1).replace("b", Accidental.FLAT_STRING);
                displayed.removeIf(s -> !s.key.toString().equals(key));
            }
            if (chord != null) {
                displayed.removeIf(s -> !s.chord.str.replace('ø','o').equals(chord));
            }
            if (inversion != null) {
                displayed.removeIf(s -> !s.inversion().equals(inversion.replace("root", "")));
            }
            if (function != null && !function.isEmpty()) {
                displayed.removeIf(s -> !(s.chord.type.str.charAt(0) == function.charAt(0)));
            }
            if (soprano != null) {
                soprano = soprano.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.SOPRANO.ordinal()]
                        .noteString(s.key).equals(soprano));
            }
            if (alto != null) {
                alto = alto.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.ALTO.ordinal()]
                        .noteString(s.key).equals(alto));
            }
            if (tenor != null) {
                tenor = tenor.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.TENOR.ordinal()]
                        .noteString(s.key).equals(tenor));
            }
            if (bass != null) {
                bass = bass.replace("#", Accidental.SHARP_STRING).replace("b", Accidental.FLAT_STRING).toUpperCase();
                displayed.removeIf(s -> !s.degrees[Voice.BASS.ordinal()]
                        .noteString(s.key).equals(bass));
            }
            List<Chord> chords = displayed.stream().map(s -> s.chord).collect(Collectors.toList());
            for (Chord c: new HashSet<>(chords)) {
                System.out.println(c + ": " + Collections.frequency(chords, c));
            }

            for (Voice v : Voice.values()) {
                System.out.println(v);
                List<String> degrees = displayed.stream().map(s -> s.degrees[v.ordinal()].noteString(s.key)).collect(Collectors.toList());
                for (String s: new HashSet<>(degrees)) {
                    System.out.println(s + ": " + Collections.frequency(degrees, s));
                }
            }
            return 0;
        }
    }
}
