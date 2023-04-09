package uwu.nyaa.owo.finalproject.data;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.im4java.core.IM4JavaException;
import org.imgscalr.Scalr;
import org.tinylog.Logger;

import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;

public class ImageProcessor
{
    /**
     * Gets the optimal image type for the given transparency
     * 
     * @param transparency
     * @return
     */
    public static int getOptimalType(int transparency)
    {
        if (transparency == Transparency.OPAQUE)
            return BufferedImage.TYPE_INT_RGB;

        // return BufferedImage.TYPE_INT_ARGB_PRE;
        return BufferedImage.TYPE_INT_ARGB;
    }

    public static BufferedImage createOptimalImage2(BufferedImage src, int width, int height)
            throws IllegalArgumentException, NullPointerException
    {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("width [" + width + "] and height [" + height + "] must be > 0");

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();

        return config.createCompatibleImage(width, height, src.getTransparency());
    }

    public static BufferedImage createOptimalImage(BufferedImage src, int width, int height)
            throws IllegalArgumentException, NullPointerException
    {
        if (width <= 0 || height <= 0)

            throw new IllegalArgumentException("width [" + width + "] and height [" + height + "] must be > 0");

        return new BufferedImage(width, height, getOptimalType(src.getTransparency()));
    }

    public static BufferedImage createOptimalImageFrom(BufferedImage src) throws IllegalArgumentException
    {
        if (src == null)
            throw new IllegalArgumentException("src cannot be null");

        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(),
                getOptimalType(src.getTransparency()));

        Graphics g = result.getGraphics();

        g.drawImage(src, 0, 0, null);

        g.dispose();

        return result;
    }

    public static BufferedImage createOptimalImageFrom2(BufferedImage src) throws IllegalArgumentException
    {
        if (src == null)
            throw new IllegalArgumentException("src cannot be null");

        BufferedImage result = createOptimalImage2(src, src.getWidth(), src.getHeight());

        Graphics g = result.getGraphics();

        g.drawImage(src, 0, 0, null);

        g.dispose();

        return result;
    }

    public static class ImageInfo
    {
        public int width;
        public int height;
        public boolean is_valid;

        public String toString()
        {
            return String.format("[%d x %d]", width, height);
        }
    }

    public static boolean saveImage(BufferedImage buff, File path, byte imgformat)
    {


        try
        {
            File path2 = path;

            if(!path2.getName().endsWith("." + FileFormat.getFileExtension(imgformat)))
            {
                path2 = new File(path.getAbsolutePath() + "." + FileFormat.getFileExtension(imgformat));
            }

            ImageMagickHelper.saveImageWithMagick(buff, path2.getAbsolutePath());

            if(path.isFile())
            {
                if(path.delete())
                {
                    path2.renameTo(path);
                }
                else
                {
                    throw new IOException("Cannot replace existing file by doing stupid rename trick with image magick");
                }
            }
            else
            {
                path2.renameTo(path);
            }
            return true;
        }
        catch (IOException | InterruptedException | IM4JavaException e)
        {
            Logger.warn(e, "Failed to save {} with image magick", path);
        }
        
        try 
        {
            ImageIO.write(buff, FileFormat.getFileExtension(imgformat), path);
            return true;
        }
        catch (IOException e) 
        {
            Logger.warn(e, "Failed to save image {} with ImageIO.write", path);
        }
        
        return false;
    }
    
    public static BufferedImage loadImage(String path)
    {
        return loadImage(new File(path));
    }

    public static BufferedImage loadImage(File path)
    {
        if (!path.isFile())
            return null;

        try
        {
            return ImageMagickHelper.loadImageWithMagick(path.getAbsolutePath());
        }
        catch (IOException | IM4JavaException | InterruptedException e)
        {
            Logger.warn(e, "Failed to load {} with ImageMagick", path);
        }

        try
        {
            BufferedImage i = ImageIO.read(path);

            if (i == null)
            {
                throw new IOException("unable to read image, ImageIO.read returned null");
            }

            return createOptimalImageFrom(i);
        }
        catch (IOException e)
        {
            Logger.warn(e,"Failed to load {}", path);
        }
        return null;
    }

    public static ImageProcessor.ImageInfo getImageInfo(File filename)
    {
        return getImageInfo(filename.getAbsolutePath());
    }

    public static ImageProcessor.ImageInfo getImageInfo(String filename)
    {
        ImageProcessor.ImageInfo simpleInfo = new ImageProcessor.ImageInfo();
        simpleInfo.width = -1;
        simpleInfo.height = -1;

        try
        {
            FFmpegProbeResult p = FFmpegHelper.FFPROBE.probe(filename);

            if (p.getStreams().size() > 0)
            {
                simpleInfo.width = p.getStreams().get(0).width;
                simpleInfo.height = p.getStreams().get(0).height;
            }

            if (simpleInfo.width != -1 && simpleInfo.height != 1)
            {
                simpleInfo.is_valid = true;
                return simpleInfo;
            }
        }
        catch (IOException e)
        {
            Logger.warn(e, "Failed to probe image {}, trying to load image instead", filename);
        }

        BufferedImage buff = ImageProcessor.loadImage(filename);

        if (buff != null)
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

        return simpleInfo;
    }

    public static void createThumbnail(File src, File dest)
    {
        BufferedImage im = loadImage(src);

        BufferedImage thumbnail = Scalr.resize(im, 128);

        im.flush();

        saveImage(thumbnail, dest, FileFormat.Image.JPG);
    }
}
