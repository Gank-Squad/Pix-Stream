package uwu.nyaa.owo.finalproject.data.filedetection;

public interface ImageFormat
{
    public static final byte UNKNOWN = -1;

    public static final byte PNG = 0;

    public static final byte JPG = 1;

    public static final byte BMP = 2;

    public static final byte TIFF = 3;

    public static final byte GIF = 4;


    /*
     * requires imagemagick / ffmpeg 
     */
    public static final byte WEBP = 7;

    public static final byte JXL = 9;

    public static final byte AVIF = 24;


    public static boolean hasNativeSupport(byte format)
    {
        switch (format)
        {
        case PNG:
        case JPG:
        case BMP:
        case GIF:
        case TIFF:
            return true;
        }

        return false;
    }

    public static String getFileExtension(byte format)
    {
        switch (format)
        {
        default:
            return "";
        case PNG:
            return "png";
        case BMP:
            return "bmp";
        case JPG:
            return "jpg";
        case GIF:
            return "gif";
        case TIFF:
            return "tiff";
        case WEBP:
            return "webp";

        case JXL:
            return "jxl";

        case AVIF:
            return "avif";

        }
    }

    public static String getMimeType(byte format)
    {
        switch (format)
        {
        default:
            return "application/unknown";
        case PNG:
            return "image/png";
        case BMP:
            return "image/bmp";
        case JPG:
            return "image/jpg";
        case GIF:
            return "image/gif";
        case TIFF:
            return "image/tiff";
        case WEBP:
            return "image/webp";

        case JXL:
            return "image/jxl";

       
        case AVIF:
            return "image/avif";
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

        case "png":
            return PNG;

        case "bmp":
            return BMP;

        case "tiff":
            return TIFF;

        case "gif":
            return GIF;

        case "jpg":
        case "jpe":
        case "jpeg":
        case "jfif":
            return JPG;

        case "webp":
            return WEBP;

        case "jxl":
            return JXL;

        case "avif":
            return AVIF;
        }
    }
}
