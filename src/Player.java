import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class Player {
    private static MidiChannel[] channels;
    private static int INSTRUMENT = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments
    private static int VOLUME = 100; // between 0 et 127

    static {
        try {
            // * Open a synthesizer
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void play(int[] melody) throws InterruptedException {
        for(int i : melody) {
            channels[INSTRUMENT].noteOn(i, VOLUME );
            Thread.sleep(400);
            channels[INSTRUMENT].noteOff(i);
        }
    }


    public static void play(State[] piece) throws InterruptedException {
        for (State s : piece) {
            play(s, 400);
        }
    }

    public static void play(State s, int duration) throws InterruptedException
    {
        // * start playing a note
        channels[INSTRUMENT].noteOn(s.midi[0], VOLUME );
        channels[INSTRUMENT].noteOn(s.midi[1], VOLUME );
        channels[INSTRUMENT].noteOn(s.midi[2], VOLUME );
        channels[INSTRUMENT].noteOn(s.midi[3], VOLUME );
        // * wait
        Thread.sleep( duration );
        // * stop playing a note
        channels[INSTRUMENT].noteOff(s.midi[0]);
        channels[INSTRUMENT].noteOff(s.midi[1]);
        channels[INSTRUMENT].noteOff(s.midi[2]);
        channels[INSTRUMENT].noteOff(s.midi[3]);
    }

}
