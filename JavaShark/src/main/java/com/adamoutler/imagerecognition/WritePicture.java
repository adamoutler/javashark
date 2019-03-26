package com.adamoutler.imagerecognition;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class WritePicture {

    public static BufferedImage underline(BufferedImage combined) {
        Graphics2D graphics = combined.createGraphics();
        graphics.setColor(Color.gray);
        graphics.drawRect(0, combined.getHeight()-3, combined.getWidth(), combined.getHeight()-1);
        graphics.dispose();
        return combined;
    }

    public  void main(String arg[]) throws Exception { //main method
        PicCoords pc = new PicCoords(0, 0, 30, 20);
        XYCoords xy = new XYCoords(5, 15);
        Font font = new Font("Bebas Neue Bold", Font.PLAIN, 30);
        BufferedImage bi = createImage(pc, xy, "-", true, Color.white, Color.green, font);

        ImageIO.write(bi, "PNG", new File("/Desktop/image.png"));
    }

    /* https://stackoverflow.com/questions/17271812/save-buffered-image-with-transparent-background*/
    /**
     * returns a buffered image of any size with text.
     *
     * @param size size of image
     * @param xyLocation location of text
     * @param text text to put into image
     * @param skipBackground true is clear, false if background should be
     * colored.
     * @param backColor color of background
     * @param foreColor color of foreground
     * @param font font to use
     * @return bufferedImage with text
     */
    public static BufferedImage createImage(PicCoords size, XYCoords xyLocation, String text, boolean skipBackground, Color backColor, Color foreColor, Font font) {
        String yourText = text;
        BufferedImage bufferedImage = new BufferedImage(size.getW(), size.getH(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        if (skipBackground) {
            graphics.setComposite(AlphaComposite.Clear);

        } else {
            graphics.setColor(backColor);
        }
        graphics.fillRect(0, 0, size.getW(), size.getH());
        graphics.setComposite(AlphaComposite.Src);
        graphics.setFont(font);
        graphics.setColor(foreColor);
        graphics.drawString(yourText, xyLocation.getX(), xyLocation.getY());

        return bufferedImage;
    }

    /**
     * prints the contents of buff2 on buff1 with the given opaque value.
     * https://codereview.stackexchange.com/questions/58067/more-efficient-way-to-draw-bufferedimage-into-another
     *
     * @param original Original image
     * @param toBeDrawnOver image to be drawn over original
     * @param opaque float percentage
     * @param x
     * @param y
     */
    public static void addImage(BufferedImage original, BufferedImage toBeDrawnOver,
            float opaque, int x, int y) {
        Graphics2D g2d = original.createGraphics();
        g2d.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
        g2d.drawImage(toBeDrawnOver, x, y, null);

        g2d.dispose();
    }

    public static void replaceColor(BufferedImage picture, int colorToReplace, int newColor, int tollerance) {
        int r = getRed(colorToReplace);
        int g = getGreen(colorToReplace);
        int b = getBlue(colorToReplace);
        for (int x = 0; x < picture.getWidth(); x++) {
            for (int y = 0; y < picture.getHeight(); y++) {
                int color = picture.getRGB(x, y);
                if (withinRange(getRed(color), r, tollerance) && withinRange(getBlue(color), b, tollerance) && withinRange(getGreen(color), g, tollerance)) {
                    picture.setRGB(x, y, newColor);

                }
            }
        }
    }

    public static BufferedImage cropExtraBackgroundOnSides(BufferedImage picture, int colorOfBackground, int tollerance, int sideMarginsPixels) {
        int r = getRed(colorOfBackground);
        int g = getGreen(colorOfBackground);
        int b = getBlue(colorOfBackground);
        int y = 0;
        int x = 0;
        boolean found = false;
        while (!found && x < picture.getWidth()-1) {
            for (y = 0; y < picture.getHeight()-1; y++) {

                int color = picture.getRGB(x, y);
                if (!withinRange(getRed(color), r, tollerance) || !withinRange(getBlue(color), b, tollerance) || !withinRange(getGreen(color), g, tollerance)) {
                    found = true;
                    break;
                }
            }
            x++;
        }
        if (x==picture.getWidth())x--;
        BufferedImage picture2 = picture.getSubimage(getLeftMargin(picture, x, sideMarginsPixels), picture.getMinY(), getLeftCropWidth(picture, x), picture.getHeight());
        found = false;
        x = picture2.getWidth();
        y = 0;
        while (!found && x > 0) {
            x--;
            for (y = 0; y < picture2.getHeight(); y++) {
                int color = picture2.getRGB(x, y);
                if (!withinRange(getRed(color), r, tollerance) || !withinRange(getBlue(color), b, tollerance) || !withinRange(getGreen(color), g, tollerance)) {
                    found = true;
                    break;
                }
            }
        }
       if (x==0)x++;

        picture = picture2.getSubimage(picture2.getMinX(), picture2.getMinY(), getRightCrop(x, sideMarginsPixels, picture2), picture2.getHeight());
        return picture;

    }

    private static int getRightCrop(int x, int sideMarginsPixels, BufferedImage picture2) {
        int rightCrop = x;
        if (x + sideMarginsPixels < picture2.getWidth()) {
            rightCrop = x + sideMarginsPixels;
        }
        return rightCrop;
    }

    private static int getLeftCropWidth(BufferedImage picture, int x) {
        int rightCropWidth = 0;
        if (picture.getWidth() - x > 0) {
            rightCropWidth = picture.getWidth() - x;
        }
        return rightCropWidth;
    }

    private static int getLeftMargin(BufferedImage picture, int x, int sideMarginsPixels) {
        int margin = 0;
        if (picture.getMinX() + x - sideMarginsPixels > 0) {
            margin = picture.getMinX() + x - sideMarginsPixels;
        }
        return margin;
    }

    static boolean withinRange(int value, int center, int minMax) {
        return (center - minMax <= value && value <= center + minMax);
    }

    public static int getRed(int rgb) {
        int blue = (rgb >> 16) & 0xFF;
        return blue;
    }

    public static int getGreen(int rgb) {

        int green = (rgb >> 8) & 0xFF;
        return green;
    }

    public static int getBlue(int rgb) {
        int red = rgb & 0xFF;
        return red;
    }

    public static int getAlpha(int rgb) {
        int red = (rgb >> 24) & 0xFF;
        return red;
    }
}
