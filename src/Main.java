import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static int[] GIANT_STEPS = new int[]{78, 74, 71, 67, 70, 71, 69, 74, 70, 67, 63, 66, 67, 65, 70, 71, 69, 74, 75, 75, 78, 79, 79, 82, 78, 78};

    public static void main(String[] args) throws Exception {
        //System.out.println(Arrays.toString(Example.WE_GIVE_THEE_BUT_THINE_OWN));
        //Player.play(Example.WE_GIVE_THEE_BUT_THINE_OWN);
        //State[] result = new State[GIANT_STEPS.length];
        //System.out.println(harmonize(GIANT_STEPS, 0, result));
        //System.out.println(Arrays.toString(result));

        //Player.play(GIANT_STEPS);
        generate();
    }

    public static void generate() throws Exception {
        State state = Example.WE_GIVE_THEE_BUT_THINE_OWN[0];
        int i = 1;
        State prev = Example.WE_GIVE_THEE_BUT_THINE_OWN[0];
        List<State> next = state.next();
        while (true) {
            System.out.println(state);
            Player.play(state, 800);
            Random randomizer = new Random();
            for (int x = 0; x < next.size(); x++) {
                //if(next.size() == 1) {
                //    break;
                //}
                if (next.get(x).passing || (state.chord.profile != null && state.chord.profile.equals(next.get(x).chord.profile))
                        || (next.get(x).seventh() && next.get(x).chord.type.equals(ChordType.TONIC)) ){//|| next.get(x).midi[3] != Example.WE_GIVE_THEE_BUT_THINE_OWN[i].midi[3]) {
                    next.remove(x);
                    x--;
                }
            }
            //System.out.println(next);
            if (next.isEmpty()) {
                System.out.println("FALLBACK");
            }

            List<State> tempNext = new ArrayList<State>();
            while(tempNext.size() < 3) {
                state = next.isEmpty() ? Example.WE_GIVE_THEE_BUT_THINE_OWN[0] : next.get(randomizer.nextInt(next.size()));
                tempNext = state.next();
            }
            next = tempNext;
            i = (i + 1) % 28;
        }
        //runTests(Example.HOMEWORK_9);
    }

    public static void runTests(State prev, State curr) {
        for (int i = 0; i < Test.tests.length; i++) {
            if (!Test.tests[i].test(prev, curr)) {
                System.err.println(prev + " -> " + curr + ": " + Test.names[i]);
            }
        }
    }

    public static void runTests(State[] piece) {
        for (int i = 0; i < piece.length - 1; i++) {
            runTests(piece[i], piece[i+1]);
        }
    }

    public static boolean harmonize(int[] melody, int starting, State[] output) {
        if (starting % 5 == 0) {
            System.out.println(starting);
        }

        if (starting == 0) {
            for (State s : State.startingChords()) {
                if (s.midi[Voice.SOPRANO.ordinal()] == melody[starting]) {
                    output[starting] = s;
                    if (harmonize(melody, starting+1, output)) {
                        return true;
                    }
                }
            }
        } else if (starting == melody.length) {
            return true;
        } else {
            for (State s : output[starting-1].next()) {
                if (s.midi[Voice.SOPRANO.ordinal()] == melody[starting]) {
                    output[starting] = s;
                    if (harmonize(melody, starting+1, output)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
