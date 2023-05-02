package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.ByteHelper;

/**
 * Detects filetypes, very useful because the file extension is a lie
 * @author minno
 *
 */
public class FileDetector
{
    /**
     * magick byte header data
     * @author minno
     *
     */
    public static class FileHeader
    {
        public byte[] header;
        public byte mime;
        public int offset;

        public FileHeader(byte[] h, byte i, int offset)
        {
            this.header = h;
            this.mime = i;
            this.offset = offset;
        }
    }

    /**
     * Combines a list of lists into one
     * @param arrs
     * @return
     */
    private static FileHeader[] combineArr(FileHeader[]... arrs)
    {
        int sum = 0;
        for (FileHeader[] h : arrs)
        {
            sum += h.length;
        }

        FileHeader[] headers = new FileHeader[sum];

        int i = 0;

        for (FileHeader[] h : arrs)
        {
            for (int j = 0; j < h.length; j++)
            {
                headers[i + j] = h[j];
            }

            i += h.length;
        }

        return headers;
    }

    /**
     * A list of all filetypes we can detect
     */
    public static final FileHeader[] HEADER_MAP = combineArr(ImageDetector.HEADER_MAP, VideoDetector.HEADER_MAP,
            AudioDetector.HEADER_MAP);

    /**
     * Gets a readable mimetype
     * @param mime
     * @return
     */
    public static String getReadableMime(byte mime)
    {
        if (FileFormat.isImageType(mime))
        {
            return FileFormat.getMimeType(mime);
        }

        if (FileFormat.isVideoType(mime))
        {
            return FileFormat.getMimeType(mime);
        }

        if (FileFormat.isAudioType(mime))
        {
            return FileFormat.getMimeType(mime);
        }

        return "application/unknown";
    }

    /**
     * Gets the mime type of whatever file the given bytes is
     * @param header The header bytes of the file 
     * @return The mime type as a byte
     */
    public static byte getFileMimeType(byte[] header)
    {
        for(FileHeader f : HEADER_MAP)
        {
            if(ByteHelper.startsWith(f.header, header, f.offset))
            {
                return f.mime;
            }
        }

        return FileFormat.UNKNOWN;
    }
    
    /**
     * Gets the mime type from an input stream,
     * THIS DOES NOT RESET THE STREAM, 
     * Always read 256 bytes, be prepared to not have that information
     * @param stream The file input stream to read from
     * @return The mime type as a byte
     */
    public static byte getFileMimeType(InputStream stream)
    {
        byte[] header;

        try
        {
            header = stream.readNBytes(256);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    
    /**
     * Gets the file mime type
     * @param file The path to the file
     * @return The mime type as a byte
     */
    public static byte getFileMimeType(File file)
    {
        byte[] header;

        try (FileInputStream fin = new FileInputStream(file))
        {
            header = fin.readNBytes(256);
        }
        catch (FileNotFoundException e)
        {
            Logger.warn(e, String.format("Cannot get file mime because %s does not exist", file));
            return FileFormat.UNKNOWN;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
