package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.StringHelper;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

/**
 * This was taken from my other github
 * https://github.com/Minnowo/Jviewer/blob/main/src/main/java/nyaa/alice/jviewer/drawing/imaging/ImageDetector.java
 * 
 * @author minno
 *
 */
public class ImageDetector
{
    public static final int MAX_HEADER_LENGTH = 12;

    public static final byte[] BMP_BYTE_IDENTIFIER = new byte[] { 0x42, 0x4D };

    public static final byte[] JPEG_BYTE_IDENTIFIER = new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF };

    // https://github.com/libjxl/libjxl/blob/main/lib/jxl/decode.cc#L92
    public static final byte[] JXL_BYTE_IDENTIFIER_1 = new byte[] { (byte) 0xFF, (byte) 0x0A };

    // https://github.com/libjxl/libjxl/blob/main/lib/jxl/decode.cc#L105
    public static final byte[] JXL_BYTE_IDENTIFIER_2 = new byte[] { 0x00, 0x00, 0x00, 0xC, 0x4A, 0x58, 0x4C, 0x20, 0xD,
            0xA, (byte) 0x87, 0xA };

    public static final byte[] PNG_BYTE_IDENTIFIER = new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A,
            0x0A };

    public static final byte[] TIFF_BYTE_IDENTIFIER_LE = new byte[] { 0x49, 0x49, 0x2A, 0x00 };

    public static final byte[] TIFF_BYTE_IDENTIFIER_BE = new byte[] { 0x4D, 0x4D, 0x00, 0x2A };

    public static final byte[] WEBP_BYTE_IDENTIFIER = new byte[] { 0x52, 0x49, 0x46, 0x46 };

    public static final byte[] GIF_BYTE_IDENTIFIER_1 = new byte[] { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61 };

    public static final byte[] GIF_BYTE_IDENTIFIER_2 = new byte[] { 0x47, 0x49, 0x46, 0x38, 0x37, 0x61 };

    // this comes after 4 bytes stating the length
    // see https://nokiatech.github.io/heif/technical.html
    public static final byte[] HEIF_FTYP_IDENTIFIER = new byte[] { 0x66, 0x74, 0x79, 0x70 };

    static class ImageFormatHeader
    {
        byte[] header;
        byte imageFormat;
        int offset;

        public ImageFormatHeader(byte[] h, byte i, int offset)
        {
            this.header = h;
            this.imageFormat = i;
            this.offset = offset;
        }
    }

    public static final ImageFormatHeader[] HEADER_MAP = new ImageFormatHeader[] {
            new ImageFormatHeader(JPEG_BYTE_IDENTIFIER, ImageFormat.JPG, 0),
            new ImageFormatHeader(PNG_BYTE_IDENTIFIER, ImageFormat.PNG, 0),
            new ImageFormatHeader(GIF_BYTE_IDENTIFIER_1, ImageFormat.GIF, 0),
            new ImageFormatHeader(GIF_BYTE_IDENTIFIER_2, ImageFormat.GIF, 0),
            new ImageFormatHeader(WEBP_BYTE_IDENTIFIER, ImageFormat.WEBP, 0),
            new ImageFormatHeader(TIFF_BYTE_IDENTIFIER_LE, ImageFormat.TIFF, 0),
            new ImageFormatHeader(TIFF_BYTE_IDENTIFIER_BE, ImageFormat.TIFF, 0),
            new ImageFormatHeader(JXL_BYTE_IDENTIFIER_1, ImageFormat.JXL, 0),
            new ImageFormatHeader(JXL_BYTE_IDENTIFIER_2, ImageFormat.JXL, 0),
            new ImageFormatHeader(BMP_BYTE_IDENTIFIER, ImageFormat.BMP, 0), };



    
    /**
     * gets the ImageFormat of the given file path
     * @param path the file path
     * @return a byte representing the ImageFormat, see ImageFormat.java
     */
    public static byte getImageFormat(String path)
    {
        File f = new File(path);

        try (FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream is = new DataInputStream(bis))
        {

            byte[] magicBytes = new byte[MAX_HEADER_LENGTH];

            for (int i = 0; i < MAX_HEADER_LENGTH; i++)
            {
                magicBytes[i] = is.readByte();

                for (ImageFormatHeader ifh : HEADER_MAP)
                {
                    if (ByteHelper.startsWith(magicBytes, ifh.header, ifh.offset))
                    {
                        return ifh.imageFormat;
                    }
                }

                if (i == 7 && ByteHelper.startsWith(magicBytes, HEIF_FTYP_IDENTIFIER, 4))
                {
                    byte[] buff = new byte[4];

                    int read = is.read(buff);

                    if (read != 4)
                        break;

                    // i'm only keeping detection for avif since it's supported by chrome
                    // i have no idea what the isobmff support is like for web browsers, but avif is for sure
                    if (Arrays.equals(buff, new byte[] { 0x61, 0x76, 0x69, 0x66, }))
                    {
                        return ImageFormat.AVIF;
                    }
                    
                    return ImageFormat.UNKNOWN;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            return ImageFormat.UNKNOWN;
        }
        catch (IOException e)
        {
            return ImageFormat.UNKNOWN;
        }
        
        return ImageFormat.UNKNOWN;
    }
}
