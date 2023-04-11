package uwu.nyaa.owo.finalproject.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.tinylog.Logger;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import uwu.nyaa.owo.finalproject.system.GlobalSettings;

/**
 * A processing class wrapping ffmpeg, implements all the stuff we need to
 * prepare video for streaming
 * 
 * @author minno
 *
 */
public class VideoProcessor
{

    /**
     * Encodes the given file to the given output file with settings designed to be
     * universally playable
     * 
     * @param input  The file to encode
     * @param output The output file
     * @throws IOException
     */
    public static void encodeUniversal(File input, File output) throws IOException
    {
        encodeUniversal(input.getAbsolutePath(), output.getAbsolutePath());
    }

    /**
     * Encodes the given file to the given output file with settings designed to be
     * universally playable
     * 
     * @param input  The file to encode
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

                .addExtraArgs("-g", "48").addExtraArgs("-keyint_min", "48")

//                .addExtraArgs("-preset", "veryfast")

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegJob job;
        if (GlobalSettings.IS_DEBUG)
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder,
                    new FFmpegHelper.FFmpegProgressHook(FFmpegHelper.FFPROBE.probe(input)));
        }
        else
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder);
        }

        job.run();
    }

    
    
    /**
     * Encodes the given file to the given output file with settings designed to be
     * universally playable
     * 
     * @param input  The file to encode
     * @param output The output file
     * @throws IOException
     */
    public static void encodeUniversalAudioOnly(File input, File output) throws IOException
    {
        encodeUniversalAudioOnly(input.getAbsolutePath(), output.getAbsolutePath());
    }

    /**
     * Encodes the given file to the given output file with settings designed to be
     * universally playable
     * 
     * @param input  The file to encode
     * @param output The output file
     * @throws IOException
     */
    public static void encodeUniversalAudioOnly(String input, String output) throws IOException
    {
        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(input).overrideOutputFiles(true).addOutput(output)

                // mp4 has shit subtitle support
                // we'll handle this later
                .disableSubtitle()
                .disableVideo()
                .setFormat("mp4")

                // playable anywhere
                .setAudioCodec("aac") // using the aac codec
                .setAudioChannels(2)

                .addExtraArgs("-g", "48")
                .addExtraArgs("-keyint_min", "48")

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegJob job;
        if (GlobalSettings.IS_DEBUG)
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder,
                    new FFmpegHelper.FFmpegProgressHook(FFmpegHelper.FFPROBE.probe(input)));
        }
        else
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder);
        }

        job.run();
    }
    
    
    /**
     * Splits the given video into tls fragments for hls streaming
     * 
     * @param input           The input video
     * @param outputDirectory The output directory for the fragments
     * @throws IOException
     */
    public static void splitVideoForHLS(File input, File outputDirectory) throws IOException
    {
        splitVideoForHLS(input.getAbsolutePath(), outputDirectory.getAbsolutePath(), null);
    }

    /**
     * Splits the given video into tls fragments for hls streaming
     * 
     * @param input           The input video
     * @param outputDirectory The output directory for the fragments
     * @param hlsSegmentSize  The target duration per fragment in seconds
     * @throws IOException
     */
    public static void splitVideoForHLS(File input, File outputDirectory, Integer hlsSegmentSize) throws IOException
    {
        splitVideoForHLS(input.getAbsolutePath(), outputDirectory.getAbsolutePath(), hlsSegmentSize);
    }

    /**
     * Splits the given video into tls fragments for hls streaming
     * 
     * @param input           The input video
     * @param outputDirectory The output directory for the fragments
     * @throws IOException
     */
    public static void splitVideoForHLS(String input, String outputDirectory) throws IOException
    {
        splitVideoForHLS(input, outputDirectory, null);
    }

    /**
     * Splits the given video into tls fragments for hls streaming
     * 
     * @param input           The input video
     * @param outputDirectory The output directory for the fragments
     * @param hlsSegmentSize  The target duration per fragment in seconds
     * @throws IOException
     */
    public static void splitVideoForHLS(String input, String outputDirectory, Integer hlsSegmentSize) throws IOException
    {
        File dir = new File(outputDirectory);

        if (!dir.isDirectory())
        {
            dir.mkdirs();
        }

        if (!dir.isDirectory())
        {
            throw new IOException("Could not make output directory for FFMPEG splitVideoForHLS");
        }

        if (hlsSegmentSize == null)
        {
            hlsSegmentSize = 5;
        }

        String outputTmpIndex = Paths.get(outputDirectory, "index.m3u8").toString();
//        String outputIndex = Paths.get(outputDirectory, "index.m3u8").toString();

        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(input).overrideOutputFiles(true).addOutput(outputTmpIndex)

                .disableSubtitle()

                .setFormat("hls")

                // no encoding is happening, just copy everything but subs
                .setAudioCodec("copy").setVideoCodec("copy")

                // really glad thsi works, cause i couldn't find the wrapped option for this
                // stuff
                .addExtraArgs("-hls_time", hlsSegmentSize.toString()).addExtraArgs("-hls_playlist_type", "vod")
                .addExtraArgs("-hls_flags", "independent_segments").addExtraArgs("-hls_segment_type", "mpegts")
                .addExtraArgs("-hls_segment_filename", Paths.get(outputDirectory, "%02d.ts").toString())
                .addExtraArgs("-master_pl_name", "master.m3u8")

                // i have no idea what this does, but it works and was there by default
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegJob job;
        if (GlobalSettings.IS_DEBUG)
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder,
                    new FFmpegHelper.FFmpegProgressHook(FFmpegHelper.FFPROBE.probe(input)));
        }
        else
        {
            job = FFmpegHelper.EXECUTOR.createJob(builder);
        }

        job.run();

        // we don't need this because the frontend handles the rest
//        try (BufferedReader br = new BufferedReader(new FileReader(outputTmpIndex));
//                FileWriter writer = new FileWriter(outputIndex, false))
//        {
//            for (String line; (line = br.readLine()) != null;)
//            {
//                if (line == null)
//                    continue;
//
//                line = line.strip();
//
//                if (line.endsWith(".ts"))
//                {
//                    writer.append("{FMT}" + line + "\n");
//                }
//                else
//                {
//                    writer.append(line + "\n");
//                }
//            }
//        }

    }

    public static void createThumbnail(File src, File dest)
    {
        int t = GlobalSettings.THUMBNAIL_SIZE;
        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput(src.getAbsolutePath()).overrideOutputFiles(true).addOutput(dest.getAbsolutePath())

                .addExtraArgs("-vf", String.format("thumbnail,scale=min(%d\\,iw*min(1\\,(%d/ih))):min(%d\\,ih*min(1\\,(%d/iw)))", t, t, t, t))
                .addExtraArgs("-vframes", "1")
                .addExtraArgs("-f", "image2")
                .addExtraArgs("-c:v", "mjpeg")

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        try
        {
            FFmpegJob job;
            if (GlobalSettings.IS_DEBUG)
            {
                job = FFmpegHelper.EXECUTOR.createJob(builder,
                        new FFmpegHelper.FFmpegProgressHook(FFmpegHelper.FFPROBE.probe(src.getAbsolutePath())));
            }
            else
            {
                job = FFmpegHelper.EXECUTOR.createJob(builder);
            }

            job.run();
        }
        catch (IOException e)
        {
            Logger.warn(e, "Failed to create thumbnail with ffmpeg for {}", src);
        }
    }

    public static VideoInfo getVideoInfo(File f)
    {
        return getVideoInfo(f.getAbsolutePath());
    }

    public static VideoInfo getVideoInfo(String f)
    {
        VideoInfo info = new VideoInfo();
        info.is_valid = true;

        try
        {
            FFmpegProbeResult p = FFmpegHelper.FFPROBE.probe(f);

            info.result = p;

            info.duration_ms = (int) (p.format.duration * 1000);

            p.getStreams().forEach(x -> {

                if(x.codec_type == null)
                    return;

                switch (x.codec_type)
                {
                case VIDEO:
                    info.hasVideo = true;
                    info.videoFormat = x.codec_name;
                    info.width = x.width;
                    info.height = x.height;
                    break;

                case AUDIO:
                    info.hasAudio = true;
                    info.audioFormat = x.codec_name;
                    break;
                }
            });
        }
        catch (IOException e)
        {
            info.is_valid = false;
            Logger.warn(e, "Error while probing {} for information", f);
        }

        return info;
    }

    public static class VideoInfo
    {
        public boolean is_valid;

        public byte mime;

        public int width;
        public int height;

        public int duration_ms;
        public int streamDuration;

        public String audioFormat;
        public String videoFormat;

        public boolean hasVideo;
        public boolean hasAudio;

        FFmpegProbeResult result;
    }
}
