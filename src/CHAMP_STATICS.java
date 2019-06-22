/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

public class CHAMP_STATICS {
    static private final Integer[][] crop_loc = {
    {525, 68, 300, 185, 0},
    {525, 48, 300, 185, 1},
    {608, 53, 300, 185, 0},
    {391, 194, 300, 185, 1},
    {317, 36, 300, 185, 1},
    {313, 107, 300, 185, 1},
    {579, 78, 300, 185, 1},
    {1029, 132, 300, 185, 1}, //ashe
    };

    public static Integer[] get_crop (int index) {
        Integer[] temp;
        temp = crop_loc[index];
        return temp;
    }
}
