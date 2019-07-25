/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class DrawData {
    /* WIP. DrawData handles graphic creation for the image displayed in DisplayWindow. However, due to the current
     * in-progress switch from client-side images to DDragon-pulled images, DrawData is non-functional.
     */
    private DataHandler data_loc;
    public static PlacedImage[] assets = new PlacedImage[0];
    public DrawData(DataHandler data){
        data_loc = data;
    }
    private static final double coord_mod = 1;


    public BufferedImage FetchCroppedPortrait(String sel_champ, int side){
        int[] champ_crop = FetchChampCrop(sel_champ);
        String champ_url = IO.MakeSafe(sel_champ);
        BufferedImage portrait = null;
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/loading/" +
                    champ_url + "_0.jpg");
            portrait = ImageIO.read(url);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        portrait = Crop(portrait, champ_crop);
        if(champ_crop[4] != side - 1){
            portrait = FlipHoriz(portrait);
        }
        portrait = Resize(portrait, 325, 105);

        return portrait;
    }

    public BufferedImage BuildGreenScreen() {
        //Internal version. Currently returns an empty green box, pending updates to DrawData.
        BufferedImage temp = new BufferedImage(1664, 936, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d_temp = temp.createGraphics();
        g2d_temp.setColor(new Color(0x3EF25F));
        g2d_temp.fillRect(0,0, temp.getWidth(), temp.getHeight());
        for(PlacedImage pi : assets){
            g2d_temp.drawImage(pi.img, pi.xloc, pi.yloc, null);
        }
        try {
            g2d_temp.drawImage(ImageIO.read(new File(".\\img\\layout.png")), 0, 0, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        g2d_temp.dispose();
        BufferedImage output = new BufferedImage(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics resizer = output.createGraphics();
        resizer.drawImage(temp, 0,0, output.getWidth(), output.getHeight(), 0, 0, temp.getWidth(null), temp.getHeight(null), null);
        resizer.dispose();

        //temp = Resize(temp, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        return output;
    }

    private int[] FetchChampCrop(String name){
        //Internal function to fetch crop data from CHAMP_STATICS.
        int[] temp = null;
        temp = CHAMP_STATICS.GetCrop(name);
        return temp;
    }

    private static BufferedImage Resize(BufferedImage i, int scaledWidth, int scaledHeight)
            {
        //Borrowed function. Resizes an image based on new coordinate measurements.
        // reads input image

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, i.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(i, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    private static BufferedImage FlipHoriz(BufferedImage i){
        //Borrowed program. Flips an image across the vertical axis.
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-i.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        i = op.filter(i, null);
        return i;
    }

    public BufferedImage FetchSquare(String champ_name){
        BufferedImage champ_image = null;
        try {
            URL target = new URL("http://ddragon.leagueoflegends.com/cdn/" + RiotAPICall.curr_version +
                    "/img/champion/" + IO.MakeSafe(champ_name) + ".png");
            champ_image = ImageIO.read(target);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return champ_image;
    }

    public void SendSelectedChamp(int side, int pick, int turn, String champ){
        if(side == 0 || pick == -1 || turn == 0){
            JOptionPane.showMessageDialog(null, "Something is horribly wrong. Champ Select Failed.");
            return;
        }
        BufferedImage new_image;
        switch(pick){
            case 1: {
                new_image = FetchCroppedPortrait(champ, side - 1);
                PlaceSelectedChamp(new_image, side, turn);
                break;
            }
            case 0:{
                new_image = FetchSquare(champ);
                PlaceBannedChamp(new_image, side, turn);
                break;
            }
        }
    }

    private void StoreAndPlace(BufferedImage i, int x, int y){
        PlacedImage p = new PlacedImage(i, x, y);
        int size = assets.length;
        PlacedImage[] temp = new PlacedImage[size + 1];
        System.arraycopy(assets, 0, temp, 0, size);
        temp[size] = p;
        assets = temp;
    }

    private BufferedImage Crop(BufferedImage original, int[] crop_data){
        BufferedImage temp = original.getSubimage(crop_data[0], crop_data[1], crop_data[2], crop_data[3]);
        BufferedImage safe_copy = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = safe_copy.createGraphics();
        g.drawImage(temp, 0,0,null);
        return safe_copy;
    }

    private void PlaceSelectedChamp(BufferedImage champ, int side, int turn){
        int x, y;
        if(side == 1){
            x = 0;
        }
        else{
            x = (int) (1336 * coord_mod);
        }
        switch(turn){
            case 1:
                y = (int) (170 * coord_mod);
                break;
            case 2:
                y = (int) (278 * coord_mod);
                break;
            case 3:
                y = (int) (387 * coord_mod);
                break;
            case 4:
                y = (int) (524 * coord_mod);
                break;
            case 5:
                y = (int) (632 * coord_mod);
                break;
            default:
                y = 0;
        }
        StoreAndPlace(champ, x, y);
    }

    private void PlaceBannedChamp(BufferedImage champ, int side, int turn){
        int xloc, yloc;
        BufferedImage skew = null;
        champ = Resize(champ, 65, 65);
        if(side == 1){
            double skewX = 0.24d;
            double x = 0;
            AffineTransform at = AffineTransform.getTranslateInstance(x, 0);
            at.shear(skewX, 0);
            AffineTransformOp op = new AffineTransformOp(at,
                    new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC));
            skew = op.filter(champ, null);
            switch(turn){
                case 1:
                    xloc = (int) (98 * coord_mod);
                    yloc = (int) (783 * coord_mod);
                    break;
                case 2:
                    xloc = (int) (168 * coord_mod);
                    yloc = (int) (783 * coord_mod);
                    break;
                case 3:
                    xloc = (int) (239 * coord_mod);
                    yloc = (int) (783 * coord_mod);
                    break;
                case 4:
                    xloc = (int) (113 * coord_mod);
                    yloc = (int) (860 * coord_mod);
                    break;
                case 5:
                    xloc = (int) (185 * coord_mod);
                    yloc = (int) (862 * coord_mod);
                    break;
                default:
                    xloc = 0;
                    yloc = 0;
                    break;
            }
        }
        else{
            double skewX = -0.24d;
            double x = -skewX * champ.getHeight();
            AffineTransform at = AffineTransform.getTranslateInstance(x, 0);
            at.shear(skewX, 0);
            AffineTransformOp op = new AffineTransformOp(at,
                    new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC));
            skew = op.filter(champ, null);
            switch(turn){
                case 1:
                    xloc = (int) (1482 * coord_mod);
                    yloc = (int) (774 * coord_mod);
                    break;
                case 2:
                    xloc = (int) (1417 * coord_mod);
                    yloc = (int) (774 * coord_mod);
                    break;
                case 3:
                    xloc = (int) (1343 * coord_mod);
                    yloc = (int) (774 * coord_mod);
                    break;
                case 4:
                    xloc = (int) (1466 * coord_mod);
                    yloc = (int) (852 * coord_mod);
                    break;
                case 5:
                    xloc = (int) (1397 * coord_mod);
                    yloc = (int) (853 * coord_mod);
                    break;
                default:
                    xloc = 0;
                    yloc = 0;
                    break;
            }
        }
        if(skew != null) {
            StoreAndPlace(skew, xloc, yloc);
        }
    }
}
