package uwu.nyaa.owo.finalproject.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

/**
 * A processing class wrapping ffmpeg, implements all the stuff we need to prepare video for streaming
 * @author minno
 *
 */
public class VideoProcessor
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

    /**
     * Encodes the given file to the given output file with settings designed to be universally playable
     * @param input The file to encode
     * @param output The output file 
     * @throws IOException
     */
    public static void encodeUniversal(File input, File output) throws IOException
    {
        encodeUniversal(input.getAbsolutePath(), output.getAbsolutePath());
    }

    /**
     * Encodes the given file to the given output file with settings designed to be universally playable
     * @param input The file to encode
     * @param output The output file 
     * @throws IOException
     */
    public static void encodeUniversal(String input, String output) throws IOException
    {
        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(input).overrideOutputFiles(true).addOutput(output)

                // mp4 has shit subtitle support
                // we'll handle this later
                .disableSubtitle().setFormat("mp4")

                // playable anywhere
                .setAudioCodec("aac") // using the aac codec
                .setAudioChannels(2)

                // playable anywhere
                .setVideoCodec("libx264") // Video using x264
                .setVideoPixelFormat("yuv420p").setVideoFrameRate(FFmpeg.FPS_23_976)

                // i'm assuming this is the crf, if it's not, literally no idea what this is 
                // 18 -> no visible quality loss
                // 23 -> standard
                .setConstantRateFactor(28)
                
                .addExtraArgs("-g", "48")
                .addExtraArgs("-keyint_min", "48")

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegJob job = EXECUTOR.createJob(builder, new FFmpegProgressHook(FFPROBE.probe(input)));

        job.run();
    }
    
    /**
     * Splits the given video into tls fragments for hls streaming
     * @param input The input video
     * @param outputDirectory The output directory for the fragments
     * @throws IOException
     */
    public static void splitVideoForHLS(File input, File outputDirectory) throws IOException
    {
        splitVideoForHLS( input.getAbsolutePath(),  outputDirectory.getAbsolutePath(),  null);
    }
    
    /**
     * Splits the given video into tls fragments for hls streaming
     * @param input The input video
     * @param outputDirectory The output directory for the fragments
     * @param hlsSegmentSize The target duration per fragment in seconds
     * @throws IOException
     */
    public static void splitVideoForHLS(File input, File outputDirectory,  Integer hlsSegmentSize) throws IOException
    {
        splitVideoForHLS( input.getAbsolutePath(),  outputDirectory.getAbsolutePath(),  hlsSegmentSize);
    }
    
    /**
     * Splits the given video into tls fragments for hls streaming
     * @param input The input video
     * @param outputDirectory The output directory for the fragments
     * @throws IOException
     */
    public static void splitVideoForHLS(String input, String outputDirectory) throws IOException
    {
        splitVideoForHLS( input,  outputDirectory,  null);
    }
    
    /**
     * Splits the given video into tls fragments for hls streaming
     * @param input The input video
     * @param outputDirectory The output directory for the fragments
     * @param hlsSegmentSize The target duration per fragment in seconds
     * @throws IOException
     */
    public static void splitVideoForHLS(String input, String outputDirectory, Integer hlsSegmentSize) throws IOException
    {
        File dir = new File(outputDirectory);
        
        if(!dir.isDirectory())
        {
            dir.mkdirs();
        }
        
        if(!dir.isDirectory())
        {
            throw new IOException("Could not make output directory for FFMPEG splitVideoForHLS");
        }
        
        if(hlsSegmentSize == null)
        {
            hlsSegmentSize = 5;
        }
        
        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(input)
                .overrideOutputFiles(true)
                .addOutput(Paths.get(outputDirectory, "index.m3u8").toString())

                .disableSubtitle()
                
                .setFormat("hls")
                
                // no encoding is happening, just copy everything but subs 
                .setAudioCodec("copy")
                .setVideoCodec("copy") 
                
                // really glad thsi works, cause i couldn't find the wrapped option for this stuff
                .addExtraArgs("-hls_time", hlsSegmentSize.toString())
                .addExtraArgs("-hls_playlist_type", "vod")
                .addExtraArgs("-hls_flags", "independent_segments")
                .addExtraArgs("-hls_segment_type", "mpegts")
                .addExtraArgs("-hls_segment_filename", Paths.get(outputDirectory, "%02d.ts").toString())
                .addExtraArgs("-master_pl_name", "master.m3u8")

                // i have no idea what this does, but it works and was there by default
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegJob job = EXECUTOR.createJob(builder, new FFmpegProgressHook(FFPROBE.probe(input)));

        job.run();
    }
}
