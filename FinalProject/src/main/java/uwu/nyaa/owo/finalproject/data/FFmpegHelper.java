package uwu.nyaa.owo.finalproject.data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

public class FFmpegHelper
{
    public static final FFmpeg FFMPEG;
    public static final FFprobe FFPROBE;
    public static final FFmpegExecutor EXECUTOR;
    
    static
    {
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
            WrappedLogger.log(Level.INFO,
                    String.format("[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx", percentage * 100,
                            progress.status, progress.frame,
                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                            progress.fps.doubleValue(), progress.speed));
        }
    }

}
