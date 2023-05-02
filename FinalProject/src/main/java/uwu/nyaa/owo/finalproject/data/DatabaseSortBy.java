package uwu.nyaa.owo.finalproject.data;

/**
 * Enum for simple database sorting 
 * @author minno
 *
 */
public interface DatabaseSortBy
{
    public static final int ASCENDING = 0b10000000;
    public static final int DECENDING = 0b00000000;
    public static final int SORT_MASK = 0b01111111;
    public static final int NONE = 0;
    public static final int RANDOM = 1;
    public static final int TIME = 2;

    public static final String RANDOM_STR = Integer.toString(RANDOM);
    
    public static boolean isAscending(int sort)
    {
        return ((ASCENDING & sort) >> 7) == 1;
    }

    
    public static boolean isValidSort(int sort)
    {
        switch (sort & SORT_MASK)
        {
        case RANDOM:
        case TIME:
        case NONE:
            return true;
        }
        return false;
    }
}
