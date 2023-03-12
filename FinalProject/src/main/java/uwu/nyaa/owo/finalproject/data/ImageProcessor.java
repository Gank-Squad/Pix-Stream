package uwu.nyaa.owo.finalproject.data;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

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
}
