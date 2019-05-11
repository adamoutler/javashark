/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.imagerecognition;

import com.adamoutler.cats.screen_id.CityKingsBattlefieldAnalysis;
import static com.adamoutler.imagerecognition.DirectTesseract.tesseractLinux;
import static com.adamoutler.imagerecognition.DirectTesseract.tesseractWindows;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adamo
 */
public class TesseractBuilder {

    /**
     * path to expected location of Windows Tesseract
     */
    public static File tesseractWindows = new File("C:\\Users\\adamo\\userContent\\jTessBoxEditor\\tesseract-ocr\\tesseract.exe");
    /**
     * path to expected location of Linux Tesseract
     */
    public static File tesseractLinux = new File("/usr/bin/tesseract");

    boolean invert = false;
    private String charset = "";//"-c","tessedit_char_whitelist=0123456789hms+"
    String language = "eng";
//,"-l","eng",
    String tessdatadir = System.getProperty("user.home") + File.separator + "userContent" + File.separator + "jTessBoxEditor" + File.separator + "samples" + File.separator + "vie" + File.separator + "tessdata";
//"--tessdata-dir",TESSDATA,
    private BufferedImage image;
    private Mode mode = Mode.AUTOMATIC;

    public enum Mode {
        ORIENTATION("0"),
        AUTOMATIC("1"),
        AUTOMATICNOOCR("2"),
        AUTOMATICNOOSD("3"),
        SINGLECOLUMN("4"),
        SINGLEBLOCK("5"),
        SINGLEBLOCKUNALIGNED("6"),
        SINGLELLINE("7"),
        SINGLEWORD("8"),
        SINGLEWORDCIRCLE("9"),
        SINGLECHAR("10");

        String value;

        Mode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * Constructor for tesseractBuilder
     *
     * @param image
     */
    public TesseractBuilder(BufferedImage image) {
        this.image = image;
    }

    public TesseractBuilder(File image) throws IOException {
        this.image = ImageIO.read(image);
    }

    public TesseractBuilder resize(float scale) {
        resize(scale, scale);
        return this;
    }

    public TesseractBuilder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public TesseractBuilder invert() {
        image = Convert.invertImage(getImage());
        return this;
    }

    public String execute() throws IOException, InterruptedException {
        File inputFile = File.createTempFile("TesseractJavaShark", ".png");
        File textFile = File.createTempFile("TesseractJavaShark", "");
        File delFile = new File(textFile.getAbsolutePath() + ".txt");
        delFile.createNewFile();
        delFile.deleteOnExit();
        inputFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(inputFile);
        ImageIO.write(getImage(), "PNG", fos);
        fos.close();
        LinkedList<String> ll = getTessArgs(inputFile, textFile);
        System.out.println("Tesseract execution: "+ ll);
                
        ProcessBuilder pb = new ProcessBuilder(ll);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        InputStream is = p.getInputStream();
        p.waitFor(5, TimeUnit.SECONDS);
        StringBuilder out = new StringBuilder();
        while (p.getInputStream().available() > 0) {
            out.append((char) p.getInputStream().read());
        }
        System.out.println(out.toString());
        StringBuilder sb = new StringBuilder();
        
        FileInputStream fis = new FileInputStream(textFile.getAbsolutePath() + ".txt");
        while (fis.available() > 0 && !sb.toString().endsWith("\n")) {
            char i = (char) fis.read();
            if (i == '\uffff') {
                break;
            }
            sb.append(i);
        }
        fis.close();
        try {
         //   textFile.delete();
           // delFile.delete();
        } catch (Exception ex) {

        }

        return sb.toString().trim();
    }

    public TesseractBuilder brightenImage() {
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        rescaleOp.filter(getImage(), getImage());  // Source and destination are the same.
        return this;
    }

    public TesseractBuilder greyScale() {
        BufferedImage nimage = new BufferedImage(getImage().getWidth(), getImage().getHeight(),
                BufferedImage.TYPE_USHORT_GRAY);
        Graphics g = nimage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = nimage;

        return this;
    }

    private LinkedList<String> getTessArgs(File inputFile, File textFile) {
        LinkedList<String> ll = new LinkedList<>();
        ll.add(getTesseractPathFile().getAbsolutePath());
        if (!language.isEmpty()) {
            ll.add("-l");
            ll.add(language);
        }
        if (!tessdatadir.isEmpty()) {
            ll.add("--tessdata-dir");
            ll.add(tessdatadir);
        }
        if (!charset.isEmpty()) {
            ll.add("-c");
            ll.add("tessedit_char_whitelist=" + getCharset());
        }
        ll.add("--psm");
        ll.add(getMode().getValue());
        ll.add("--dpi");
        ll.add("100");
        ll.add(inputFile.getAbsolutePath());
        ll.add(textFile.getAbsolutePath());
        return ll;
    }

    public TesseractBuilder crop(PicCoords pc) {
        image = pc.getImageAtCoords(getImage());
        return this;
    }

    public static File getTesseractPathFile() {

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return tesseractWindows;
        }

        return tesseractLinux;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    public TesseractBuilder writePicture(PicCoords pc, String name) {

        try {

            String filename = System.getProperty("java.io.tmpdir") + "citykingsbattlefieldanalysis" + name + ".png";
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                ImageIO.write(pc.getImageAtCoords(image), "PNG", fos);
            }
            System.out.println("wrote " + filename);
        } catch (IOException ex) {
            Logger.getLogger(CityKingsBattlefieldAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public TesseractBuilder antiAlias() {
        filter(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return this;
    }

    public TesseractBuilder bilinear() {
        filter(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        return this;
    }

    public TesseractBuilder bicubic() {
        filter(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return this;
    }

    public TesseractBuilder resize(float xValue, float yValue) {
        BufferedImage nimage = new BufferedImage(
                Math.round(getImage().getWidth() * xValue),
                Math.round(getImage().getHeight() * yValue),
                image.getType());
        Graphics2D g2d = (Graphics2D) nimage.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        float sx = (float) nimage.getWidth() / image.getWidth();
        float sy = (float) nimage.getHeight() / image.getHeight();
        g2d.scale(sx, sy);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        image = nimage;
        return this;
    }

    public TesseractBuilder filter(Key key, Object value) {
        BufferedImage nimage = new BufferedImage(getImage().getWidth(), getImage().getHeight(), image.getType());
        Graphics2D g2d = (Graphics2D) nimage.getGraphics();
        g2d.setRenderingHint(key, value);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        image = nimage;
        return this;
    }

    public TesseractBuilder writePicture(String name) {
        PicCoords pc = new PicCoords(0, 0, image.getWidth(), image.getHeight());
        writePicture(pc, name);
        return this;
    }

    public TesseractBuilder writePicture() {
        PicCoords pc = new PicCoords(0, 0, image.getWidth(), image.getHeight());
        writePicture(pc, "");
        return this;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public TesseractBuilder setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

}
