package uwu.nyaa.owo.finalproject;

import uwu.nyaa.owo.finalproject.data.PathHelper;

public class Program
{
    public static void main(String[] args)
    {
        System.out.println(PathHelper.MEDIA_DIR_BASE);
        
        String input = "/mnt/Data/0_VIDEO/VTUBERS/Shondo/1601284004506112002_1.mp4";
//
        String output = "/home/minno/Videos/uwu.mp4";
//
        String outputDir = "/home/minno/Videos/uwu/";
        

        
        String sha256Hex = PathHelper.getMediaPath(input);
        
        System.out.println(sha256Hex);
        
//        
//        try
//        {
//            VideoProcessor.encodeUniversal(input, output);
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//        try
//        {
//            VideoProcessor.splitVideoForHLS(output, outputDir);
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}
