package uwu.nyaa.owo.finalproject.data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.tinylog.Logger;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

public class FFmpegHelper
{
    public static  FFmpeg FFMPEG;
    public static FFprobe FFPROBE;
    public static  FFmpegExecutor EXECUTOR;
 
    public static void checkFFmpeg()
    {
        GlobalSettings.updatePathsForLinux();

        try
        {
            FFMPEG = new FFmpeg(GlobalSettings.FFMPEG_PATH);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            FFPROBE = new FFprobe(GlobalSettings.FFPROBE_PATH);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        EXECUTOR = new FFmpegExecutor(FFMPEG, FFPROBE);
    }
    

    /**
     * A progress hook used to display ffmpeg progress and information while encoding
     * @author minno
     *
     */
    public static class FFmpegProgressHook implements ProgressListener
    {
        public final FFmpegProbeResult probe;
        public final double duration_ns;

        public FFmpegProgressHook(FFmpegProbeResult probe)
        {
            this.probe = probe;
            this.duration_ns = probe.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
        }

        public void progress(Progress progress)
        {
            double percentage = progress.out_time_ns / duration_ns;

            // Print out interesting information about the progress
            Logger.info(
                    String.format("[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx", percentage * 100,
                            progress.status, progress.frame,
                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                            progress.fps.doubleValue(), progress.speed));
        }
    }

}
