/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

import java.awt.image.BufferedImage;

public class PlacedImage {
    // Currently unused. Will be used in DrawData when implementation there is properly dealt with.
    BufferedImage img;
    int xloc;
    int yloc;

    public PlacedImage(BufferedImage i, int x, int y){
        img = i;
        xloc = x;
        yloc = y;
    }
}
