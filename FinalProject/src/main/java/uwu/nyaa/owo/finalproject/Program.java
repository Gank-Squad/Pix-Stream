package uwu.nyaa.owo.finalproject;

import java.io.IOException;

import uwu.nyaa.owo.finalproject.data.VideoProcessor;

public class Program
{
    public static void main(String[] args)
    {
        String input = "/mnt/Data/0_VIDEO/VTUBERS/Shondo/1601284004506112002_1.mp4";

        String output = "/home/minno/Videos/uwu.mp4";

        String outputDir = "/home/minno/Videos/uwu/";
        
        try
        {
            VideoProcessor.encodeUniversal(input, output);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try
        {
            VideoProcessor.splitVideoForHLS(output, outputDir);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
