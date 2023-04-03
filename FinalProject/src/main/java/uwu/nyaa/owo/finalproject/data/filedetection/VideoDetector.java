package uwu.nyaa.owo.finalproject.data.filedetection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.tinylog.Logger;

import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector.FileHeader;

public class VideoDetector
{
    public static final int MAX_HEADER_LENGTH = 12;

    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_1 = "ftypmp4".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_2 = "ftypisom".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_3 = "ftypM4V".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_4 = "ftypMSNV".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_5 = "ftypavc1".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_6 = "ftypFACE".getBytes();
    public static final byte[] UNDERTERMINED_VIDEO_MP4_HEADER_7 = "ftypdash".getBytes();

    public static final byte[] FLV_HEADER = "FLV".getBytes();
    public static final byte[] MOV_HEADER = "ftypqt".getBytes();
    public static final byte[] AVI_HEADER = "AVI ".getBytes();
    public static final byte[] MKV_HEADER = new byte[] { 0x1a, 0x45, (byte) 0xdf, (byte) 0xa3 };

    public static final byte[] UNDERTERMINED_VIDEO_WM_HEADER = new byte[] { 0x30, 0x26, (byte) 0xb2, 0x75, (byte) 0x8e,
            0x66, (byte) 0xcf, 0x11, (byte) 0xa6, (byte) 0xd9, 0x00, (byte) 0xaa, 0x00, 0x62, (byte) 0xce, 0x6c };

    public static final FileHeader[] HEADER_MAP = new FileHeader[] {
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_1, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_2, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_3, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_4, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_5, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_6, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(UNDERTERMINED_VIDEO_MP4_HEADER_7, FileFormat.Video.UNDETERMINED_MP4, 4),
            new FileHeader(FLV_HEADER, FileFormat.Video.FLV, 0),
            new FileHeader(MOV_HEADER, FileFormat.Video.MOV, 4),
            new FileHeader(AVI_HEADER, FileFormat.Video.AVI, 8),
            new FileHeader(MKV_HEADER, FileFormat.Video.MKV, 0), };

    public static byte getVideoFormat(File file)
    {
        byte[] header;

        try (FileInputStream fin = new FileInputStream(file))
        {
            header = fin.readNBytes(24);
        }
        catch (FileNotFoundException e)
        {
            Logger.warn(e,String.format("Cannot get file mime because %s does not exist", file));
            return FileFormat.UNKNOWN;
        }
        catch (IOException e)
        {
            Logger.warn(e,String.format("IOException while trying to get file mime of %s", file));
            return FileFormat.UNKNOWN;
        }

        for (FileHeader f : HEADER_MAP)
        {
            if (ByteHelper.startsWith(f.header, header, f.offset))
            {
                return f.mime;
            }
        }

        return FileFormat.UNKNOWN;
    }
}
