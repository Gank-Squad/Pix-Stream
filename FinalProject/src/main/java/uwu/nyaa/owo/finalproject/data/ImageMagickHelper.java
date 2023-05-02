package uwu.nyaa.owo.finalproject.data;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.core.Operation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.ProcessStarter;
import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

/**
 * Used for handling image magick
 * @author minno
 *
 */
public class ImageMagickHelper
{
    public static final String IMAGE_DECODE_FORMAT = "bmp";

    // like 95% sure i used this wrong
    public static double JPEG_QUALITY = 95;

    /**
     * Checks if image magick is found, otherwise throws runtime exception
     * @return
     */
    public static boolean checkImageMagick()
    {
        ImageCommand cmd = new ImageCommand();

        Operation op = new Operation();

        op.addRawArgs("magick", "--version");

        try
        {
            cmd.run(op);
            return true;
        }
        catch (IOException | InterruptedException | IM4JavaException e)
        {
            Logger.error(e, "Could not run 'magick --version' something is wrong");
            throw new RuntimeException("Could not run 'magick --version' something is wrong", e);
        }
    }

    /**
     * Loads an image to a java object using imagemagick
     * @param path The path to the image
     * @return A BufferedImage 
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    public static BufferedImage loadImageWithMagick(String path)
            throws IOException, InterruptedException, IM4JavaException
    {
        IMOperation op = new IMOperation();

        Logger.info(path);
        // input image path, take only first image in the file
        op.addImage(path + "[0]");

        // set image output type into stdout, (bmp seems fastest but slow to render)
        // who cares if it's slow to render, that only matters if *we're* doing it in java
        // we're not and we optimize it anyway down below
        op.addImage(IMAGE_DECODE_FORMAT + ":-");

        // set up command
        ConvertCmd convert = new ConvertCmd();
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);

        // run command and extract BufferedImage from OutputConsumer
        convert.run(op);

        if (s2b.getImage() == null)
            throw new IM4JavaException("null image recieved from magick");

        return ImageProcessor.createOptimalImageFrom(s2b.getImage());
    }

    
    /**
     * Saves an image with image magick
     * @param buf The image to save
     * @param path The location to save it 
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    public static void saveImageWithMagick(BufferedImage buf, String path)
            throws IOException, InterruptedException, IM4JavaException
    {
        if (buf == null)
            return;

        IMOperation op = new IMOperation();

        op.addImage(); // input

        if(FileFormat.getFromFileExtension(StringHelper.getFileExtension(path)) == FileFormat.Image.JPG)
        {
            // i swear this doesn't work
            op.quality(JPEG_QUALITY);
        }

        op.addImage(path); // outputs


        ConvertCmd convert = new ConvertCmd();
        

        convert.run(op, buf);
    }

    
    /**
     * DEBUG ONLY
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    public static void main(String[] args) throws IOException, InterruptedException, IM4JavaException
    {
        ProcessStarter.setGlobalSearchPath("C:\\bin\\imageMagick");

        String test = "C:\\bin\\1.png";
        checkImageMagick();

        FileProcessor.addFile(test);
    }
}
