package uwu.nyaa.owo.finalproject.data;

/**
 * Enum for simple database sorting 
 * @author minno
 *
 */
public interface DatabaseSortBy
{
    public static final int NONE = 0;
    public static final int RANDOM = 1;
    public static final int TIME = 2;
    
    
    public static boolean isValidSort(int sort)
    {
        switch (sort)
        {
        case RANDOM:
        case TIME:
        case NONE:
            return true;
        }
        return false;
    }
}
