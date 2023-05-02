package uwu.nyaa.owo.finalproject.data.filedetection;

import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector.FileHeader;

/**
 * Information used to detect audio files
 * @author minno
 *
 */
public class AudioDetector
{
    public static final byte[] FLAC_HEADER = "fLaC".getBytes();
    public static final byte[] WAV_HEADER_1 = "RIFF".getBytes();
    public static final byte[] WAV_HEADER_2 = "WAVE".getBytes();
    
    public static final byte[] MP3_HEADER_1 = new byte[] { (byte)0xFF, (byte)0xFB };
    public static final byte[] MP3_HEADER_2 = new byte[] { (byte)0xFF, (byte)0xF3 };
    public static final byte[] MP3_HEADER_3 = new byte[] { (byte)0xFF, (byte)0xF2 };
    public static final byte[] MP3_HEADER_4 = new byte[] { (byte)0x49, (byte)0x44, 0x33 };
    
    public static final byte[] OGG_HEADER = "OggS".getBytes();

    public static final FileHeader[] HEADER_MAP = new FileHeader[] {
            new FileHeader(FLAC_HEADER, FileFormat.Audio.FLAC, 0),
            new FileHeader(WAV_HEADER_1, FileFormat.Audio.WAV, 0),
            new FileHeader(WAV_HEADER_2, FileFormat.Audio.WAV, 0),
            new FileHeader(MP3_HEADER_1, FileFormat.Audio.MP3, 0),
            new FileHeader(MP3_HEADER_2, FileFormat.Audio.MP3, 0),
            new FileHeader(MP3_HEADER_3, FileFormat.Audio.MP3, 0),
            new FileHeader(MP3_HEADER_4, FileFormat.Audio.MP3, 0),
            new FileHeader(OGG_HEADER, FileFormat.Audio.OGG, 0),
            };

}
