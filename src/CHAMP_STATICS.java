/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

public class CHAMP_STATICS {
    /* CURRENTLY UNDER RENOVATION.
     * A static class used to hold the crop details of the champion portraits pulled from DDragon for champion
     * select. When possible, the full image will be used instead, but this is not an option due to how DisplayWindow
     * is built.
     */
    static private final Integer[][] crop_loc = {
    };

    public static Integer[] get_crop (int index) {
        Integer[] temp;
        temp = crop_loc[index];
        return temp;
    }
}
