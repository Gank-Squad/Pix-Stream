package uwu.nyaa.owo.finalproject.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.im4java.core.Operation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.ProcessStarter;

import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

import javax.imageio.ImageIO;

public class ImageMagickHelper
{
    public static final String IMAGE_DECODE_FORMAT = "bmp";

    static
    {
        if (ProcessStarter.getGlobalSearchPath() == null)
        {
            WrappedLogger.log(Level.INFO, "could not find environmental variable IM4JAVA_TOOLPATH, using PATH instead");

            ProcessStarter.setGlobalSearchPath(System.getenv("PATH"));

        }
        else
        {
            WrappedLogger.log(Level.INFO,
                    "global magick search path set [" + ProcessStarter.getGlobalSearchPath() + "]");
        }
    }

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
            WrappedLogger.warning("Could not run 'magick --version' something is wrong", e);
            throw new RuntimeException("Could not run 'magick --version' something is wrong", e);
        }
    }

    public static ImageProcessor.ImageInfo getImageInfo(String filename)
    {
        ImageProcessor.ImageInfo simpleInfo = new ImageProcessor.ImageInfo();

        // // this doesn't work for some reason, so i'm just loading the image cause idrc
//        Info imageInfo;
//        try
//        {
//            imageInfo = new Info(filename, true);
//        }
//        catch (InfoException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return simpleInfo;
//        }
//
//        try
//        {
//            simpleInfo.width = imageInfo.getImageWidth();
//        }
//        catch (InfoException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try
//        {
//            simpleInfo.height = imageInfo.getImageHeight();
//        }
//        catch (InfoException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        simpleInfo.magickFormat = imageInfo.getImageFormat();

        
        
        try
        {
            BufferedImage buff = loadImageWithMagick(filename);

            if(buff != null)
            {
                simpleInfo.width = buff.getWidth();
                simpleInfo.height = buff.getHeight();
                simpleInfo.is_valid = true;
                buff.flush();
                buff = null;
            }
            else
            {
                simpleInfo.is_valid = false;
            }
        }
        catch (IOException | InterruptedException | IM4JavaException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        return simpleInfo;
    }

    public static BufferedImage loadImageWithMagick(String path)
            throws IOException, InterruptedException, IM4JavaException
    {
        IMOperation op = new IMOperation();

        WrappedLogger.info(path);
        // input image path, take only first image in the file
        op.addImage(path + "[0]");

        // set image output type into stdout, (bmp seems fastest but slow to render)
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



    public static void main(String[] args) throws IOException, InterruptedException, IM4JavaException
    {
        ProcessStarter.setGlobalSearchPath("C:\\bin\\imageMagick");

        String test = "C:\\bin\\1.png";
checkImageMagick();

        FileProcessor.addFile(test);
    }
}
