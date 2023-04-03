package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.ByteHelper;

public class FileDetector
{
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

    public static final FileHeader[] HEADER_MAP = combineArr(ImageDetector.HEADER_MAP, VideoDetector.HEADER_MAP,
            AudioDetector.HEADER_MAP);

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
