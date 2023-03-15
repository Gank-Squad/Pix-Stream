package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector.FileHeader;
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

    // // starts from offset 0, means RIFF, this is the same as for wav files
    // public static final byte[] WEBP_BYTE_IDENTIFIER = new byte[] { 0x52, 0x49, 0x46, 0x46 };
    // // starts from offset 8, means WEBP, should be unique
    public static final byte[] WEBP_BYTE_IDENTIFIER = new byte[] { 0x57, 0x45, 0x42, 0x50 };

    public static final byte[] GIF_BYTE_IDENTIFIER_1 = new byte[] { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61 };

    public static final byte[] GIF_BYTE_IDENTIFIER_2 = new byte[] { 0x47, 0x49, 0x46, 0x38, 0x37, 0x61 };

    // this comes after 4 bytes stating the length
    // see https://nokiatech.github.io/heif/technical.html
    public static final byte[] HEIF_FTYP_IDENTIFIER = new byte[] { 0x66, 0x74, 0x79, 0x70 };

    public static final byte[] AVIF_IDENTIFIER = new byte[] { 0x61, 0x76, 0x69, 0x66, };

    public static final FileHeader[] HEADER_MAP = new FileHeader[] {
            new FileHeader(JPEG_BYTE_IDENTIFIER, FileFormat.Image.JPG, 0),
            new FileHeader(PNG_BYTE_IDENTIFIER, FileFormat.Image.PNG, 0),
            new FileHeader(GIF_BYTE_IDENTIFIER_1, FileFormat.Image.GIF, 0),
            new FileHeader(GIF_BYTE_IDENTIFIER_2, FileFormat.Image.GIF, 0),
            new FileHeader(WEBP_BYTE_IDENTIFIER, FileFormat.Image.WEBP, 8),
            new FileHeader(TIFF_BYTE_IDENTIFIER_LE, FileFormat.Image.TIFF, 0),
            new FileHeader(TIFF_BYTE_IDENTIFIER_BE, FileFormat.Image.TIFF, 0),
            new FileHeader(JXL_BYTE_IDENTIFIER_1, FileFormat.Image.JXL, 0),
            new FileHeader(JXL_BYTE_IDENTIFIER_2, FileFormat.Image.JXL, 0),
            new FileHeader(BMP_BYTE_IDENTIFIER, FileFormat.Image.BMP, 0),
            new FileHeader(AVIF_IDENTIFIER, FileFormat.Image.AVIF, 8)
            };



    
    public static byte getImageFormat(File file)
    {
        byte[] header;

        try (FileInputStream fin = new FileInputStream(file))
        {
            header = fin.readNBytes(24);
        }
        catch (FileNotFoundException e)
        {
            WrappedLogger.warning(String.format("Cannot get file mime because %s does not exist", file));
            return FileFormat.UNKNOWN;
        }
        catch (IOException e)
        {
            WrappedLogger.warning(String.format("IOException while trying to get file mime of %s", file), e);
            return FileFormat.UNKNOWN;
        }

        
        for(FileHeader f : HEADER_MAP)
        {
            if(ByteHelper.startsWith(f.header, header, f.offset))
            {
                return f.mime;
            }
        }
        
        return FileFormat.UNKNOWN;
    }
}
