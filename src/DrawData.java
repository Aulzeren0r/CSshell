/*
 * Copyright (c) Chris Attias, $month.year. Use of this code is reserved solely for the University of Alabama League of Legends Club.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawData {
    private DataHandler data_loc;

    public DrawData(DataHandler data){
        data_loc = data;
    }

    public BufferedImage FetchCroppedPortrait(Champ sel_champ, int side){
        BufferedImage temp = null;
        try{
            temp = ImageIO.read(new File(sel_champ.loc));
        } catch(IOException e){
            e.printStackTrace();
        }
        Integer[] crop_arr = FetchChampCrop(sel_champ.name);

        temp = temp.getSubimage(crop_arr[0], crop_arr[1], crop_arr[2], crop_arr[3]);
        try {
            temp = Resize(temp, 250, 100);
        } catch(IOException e){
            e.printStackTrace();
        }

        switch(crop_arr[4] - side){
            case 1:
            case -1:
                temp = FlipHoriz(temp);
                break;
            default:
                break;
        }
        return temp;
    }

    public BufferedImage BuildGreenScreen(PlacedImage[] image_array){
        BufferedImage temp = BuildGreenScreen();
        return temp;
    }

    private BufferedImage BuildGreenScreen() {
        BufferedImage temp = new BufferedImage(650, 650, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d_temp = temp.createGraphics();
        g2d_temp.setColor(new Color(0x3EF25F));
        g2d_temp.fillRect(0,0, 650, 650);
        g2d_temp.dispose();
        return temp;
    }

    private Integer[] FetchChampCrop(String name){
        int i = 0;
        while(!name.equals(data_loc.champ_array[i].name)){
            i++;
        }
        Integer[] temp = CHAMP_STATICS.get_crop(i);
        return temp;
    }

    private static BufferedImage Resize(BufferedImage i, int scaledWidth, int scaledHeight)
            throws IOException {
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
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-i.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        i = op.filter(i, null);
        return i;
    }

}
