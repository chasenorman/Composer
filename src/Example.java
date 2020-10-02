public class Example {
    public static final State[] WE_GIVE_THEE_BUT_THINE_OWN = State.piece(
            new int[]{74, 62, 59, 55}, new int[]{72, 66, 57, 50}, new int[]{71, 67, 55, 52}, new int[]{69, 64, 55, 48}, new int[]{71, 62, 54, 50}, new int[]{72, 62, 54, 50}, new int[]{71, 62, 55, 43},
            new int[]{71, 62, 55, 43}, new int[]{69, 62, 54, 50}, new int[]{62, 62, 54, 47}, new int[]{64, 62, 59, 43}, new int[]{64, 61, 57, 45}, new int[]{62, 62, 54, 50},
            new int[]{62, 62, 55, 47}, new int[]{64, 60, 55, 48}, new int[]{66, 60, 57, 45}, new int[]{67, 62, 55, 47}, new int[]{71, 62, 55, 43}, new int[]{69, 64, 55, 48}, new int[]{69, 64, 54, 48}, new int[]{71, 63, 54, 47},
            new int[]{71, 64, 56, 52}, new int[]{72, 64, 57, 45}, new int[]{71, 67, 62, 47}, new int[]{69, 67, 64, 48}, new int[]{69, 66, 62, 50}, new int[]{69, 66, 60, 50}, new int[]{67, 62, 59, 43}
            );

    public static final State[] HOMEWORK_9 = State.piece(
            new int[]{65, 60, 56, 53}, new int[]{68, 60, 54, 51}, new int[]{68, 61, 53, 49}, new int[]{70, 65, 56, 50}, new int[]{72, 65, 56, 50},
            new int[]{70, 67, 55, 51}, new int[]{70, 65, 55, 49}, new int[]{72, 63, 56, 48}, new int[]{73, 63, 55, 46},
            new int[]{72, 63, 56, 44}, new int[]{72, 63, 54, 45}, new int[]{70, 61, 53, 46}, new int[]{68, 62, 53, 47},
            new int[]{68, 60, 53, 48}, new int[]{67, 60, 52, 48}, new int[]{67, 58, 52, 48}, new int[]{65, 56, 53, 41}
    );


    static {
        HOMEWORK_9[HOMEWORK_9.length -3].passing = false;
        HOMEWORK_9[HOMEWORK_9.length -3].cadence = false;
    }
}
