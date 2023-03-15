package uwu.nyaa.owo.finalproject.data.filedetection;

import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector.FileHeader;

public class AudioDetector
{
    public static final byte[] FLAC_HEADER = "fLaC".getBytes();
    public static final byte[] WAV_HEADER_1 = "RIFF".getBytes();
    public static final byte[] WAV_HEADER_2 = "WAVE".getBytes();

    public static final FileHeader[] HEADER_MAP = new FileHeader[] {
            new FileHeader(FLAC_HEADER, FileFormat.Audio.FLAC, 0),
            new FileHeader(WAV_HEADER_1, FileFormat.Audio.WAV, 0),
            new FileHeader(WAV_HEADER_2, FileFormat.Audio.WAV, 0), };

}
