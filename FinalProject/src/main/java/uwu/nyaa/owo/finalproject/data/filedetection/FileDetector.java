package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.File;

public class FileDetector
{
//    TODO: add a method here to use ImageDetector, VideoDetector and AudioDetector to determine file format
    
    
    public static byte getFileMimeType(File f)
    {
        if(!f.isFile())
        {
            return ImageFormat.UNKNOWN;
        }
        
        return 0;
    }

}
