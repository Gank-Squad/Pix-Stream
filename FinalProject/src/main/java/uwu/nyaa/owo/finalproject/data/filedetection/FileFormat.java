package uwu.nyaa.owo.finalproject.data.filedetection;

public interface FileFormat
{
    public static final byte UNKNOWN = -1;

    public interface Image
    {
        public static final byte PNG = 0;
        public static final byte JPG = 1;
        public static final byte BMP = 2;
        public static final byte TIFF = 3;
        public static final byte GIF = 4;

        /*
         * requires imagemagick / ffmpeg
         */
        public static final byte WEBP = 5;
        public static final byte JXL = 6;
        public static final byte AVIF = 7;
    }

    public interface Video
    {
        public static final byte UNDETERMINED_MP4 = 20;

        public static final byte FLV = 21;
        public static final byte MOV = 22;
        public static final byte AVI = 23;
        public static final byte MKV = 24;

    }

    public interface Audio
    {
        public static final byte FLAC = 40;
        public static final byte WAV = 41;
        public static final byte MP3 = 42;
        public static final byte OGG = 43;

    }

    public static boolean hasNativeSupport(byte format)
    {
        switch (format)
        {
        case Image.PNG:
        case Image.JPG:
        case Image.BMP:
        case Image.GIF:
        case Image.TIFF:
            return true;
        }

        return false;
    }

    public static boolean isAudioType(byte mimeType)
    {
        switch (mimeType)
        {
        default:
            return false;
        case Audio.MP3:
        case Audio.FLAC:
        case Audio.WAV:
        case Audio.OGG:
            return true;
        }
    }

    public static boolean isImageType(byte mimeType)
    {
        switch (mimeType)
        {
        default:
            return false;
        case Image.PNG:
        case Image.BMP:
        case Image.JPG:
        case Image.GIF:
        case Image.TIFF:
        case Image.WEBP:
        case Image.JXL:
        case Image.AVIF:
            return true;
        }
    }

    public static boolean isVideoType(byte mimeType)
    {
        switch (mimeType)
        {
        default:
            return false;

        case Video.FLV:
        case Video.MOV:
        case Video.AVI:
        case Video.MKV:
        case Video.UNDETERMINED_MP4:
            return true;
        }
    }

    public static String getMimeType(byte format)
    {
        switch (format)
        {
        default:
            return "application/unknown";

        case Image.PNG:
            return "image/png";
        case Image.BMP:
            return "image/bmp";
        case Image.JPG:
            return "image/jpg";
        case Image.GIF:
            return "image/gif";
        case Image.TIFF:
            return "image/tiff";
        case Image.WEBP:
            return "image/webp";
        case Image.JXL:
            return "image/jxl";
        case Image.AVIF:
            return "image/avif";

        case Video.FLV:
            return "video/flv";
        case Video.MOV:
            return "video/mov";
        case Video.AVI:
            return "video/avi";
        case Video.MKV:
            return "video/mkv";
        case Video.UNDETERMINED_MP4:
            return "video/mp4";

        case Audio.FLAC:
            return "audio/flac";
        case Audio.WAV:
            return "audio/wav";
        case Audio.MP3:
            return "audio/mp3";
        case Audio.OGG:
            return "audio/ogg";

        }
    }

    public static byte getFromFileExtension(String ext)
    {
        if (ext.startsWith("."))
            ext = ext.substring(1);

        // krita usually puts ~ at the end if the file was overwritten with a newer copy
        // i'm assuming like 99% of file formats don't have ~ so i'm gonna remove it
        // here
        // and assume krita or something else put it there for this reason
        if (ext.endsWith("~"))
            ext = ext.substring(0, ext.length() - 1);

        switch (ext.toLowerCase())
        {
        default:
            return UNKNOWN;

        case "mkv":
            return Video.MKV;
        case "mov":
            return Video.MOV;
        case "flv":
            return Video.FLV;
        case "avi":
            return Video.AVI;
        case "mp4":
            return Video.UNDETERMINED_MP4;
        case "flac":
            return Audio.FLAC;
        case "wav":
            return Audio.WAV;

        case "png":
            return Image.PNG;
        case "bmp":
            return Image.BMP;
        case "tiff":
            return Image.TIFF;
        case "gif":
            return Image.GIF;

        case "jpg":
        case "jpe":
        case "jpeg":
        case "jfif":
            return Image.JPG;

        case "webp":
            return Image.WEBP;

        case "jxl":
            return Image.JXL;

        case "avif":
            return Image.AVIF;
            
        case "mp3":
            return Audio.MP3;
        case "ogg":
            return Audio.OGG;
        }
    }

    public static String getFileExtension(byte format)
    {
        switch (format)
        {
        default:
            return "";
        case Image.PNG:
            return "png";
        case Image.BMP:
            return "bmp";
        case Image.JPG:
            return "jpg";
        case Image.GIF:
            return "gif";
        case Image.TIFF:
            return "tiff";
        case Image.WEBP:
            return "webp";

        case Image.JXL:
            return "jxl";

        case Image.AVIF:
            return "avif";
        case Video.MKV:
            return "mkv";
        case Video.MOV:
            return "mov";
        case Video.FLV:
            return "flv";
        case Video.AVI:
            return "avi";
        case Video.UNDETERMINED_MP4:
            return "mp4";
        case Audio.FLAC:
            return "flac";
        case Audio.WAV:
            return "wav";
        case Audio.MP3:
            return "mp3";
        case Audio.OGG:
            return "ogg";
        }
    }
}

