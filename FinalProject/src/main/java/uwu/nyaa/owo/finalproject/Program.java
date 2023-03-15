package uwu.nyaa.owo.finalproject;

import java.io.File;

import uwu.nyaa.owo.finalproject.data.FFmpegHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.VideoProcessor;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.ImageDetector;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector.FileHeader;

public class Program
{
    
    private static FileHeader[] combineArr(FileHeader[] ...arrs)
    {
        int sum = 0;
        for(FileHeader[] h : arrs)
        {
            sum += h.length;
        }

        FileHeader[] headers = new FileHeader[sum];
        
        int i = 0;
        
        for(FileHeader[] h : arrs)
        {
            for(int j = 0; j < h.length; j++)
            {
                headers[i + j] = h[j];
            }
            
            i += h.length;
        }

        return headers;
    }
    
    public static void main(String[] args)
    {
        GlobalSettings.IS_DEBUG = true;
        GlobalSettings.updatePathsForLinux();
        FFmpegHelper.checkFFmpeg();
        String[] tests = new String[] {
                "/home/minno/Pictures/010.jpg",
                "/home/minno/Pictures/010.png",
                "/home/minno/Pictures/010.webp",
                "/home/minno/Pictures/010.tiff",
                "/home/minno/Pictures/010.jxl",
                "/home/minno/Pictures/010.gif",
                "/home/minno/Pictures/010.bmp",
                "/home/minno/Pictures/010.avif",
                "/home/minno/Pictures/010.mkv",
                "/home/minno/Pictures/010.mov",
                "/home/minno/Pictures/010.mp4",
                "/home/minno/Pictures/010.flv",
                "/home/minno/Pictures/010.flac",
                "/home/minno/Pictures/010.wav",
        };
   
        
//        for(String file : tests)
//        {
//            byte mime = FileDetector.getFileMimeType(new File(file));    
//            
//            System.out.println("File %s was detected as %s".formatted(file, FileDetector.getReadableMime(mime)));
//        }
//        
        String t =  "/home/minno/Pictures/0100.mkv";
        FileProcessor.addFile(new File(t));
//        VideoProcessor.getVideoInfo(new File(t));
    }
}
