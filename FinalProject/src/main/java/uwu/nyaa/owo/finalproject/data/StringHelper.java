package uwu.nyaa.owo.finalproject.data;

import java.io.File;

/**
 * useful string manip
 * @author minno
 *
 */
public class StringHelper
{
    /**
     * gets the file extension from a string
     * @param name the filename / path
     * @param includeDot should the extension include a dot
     * @return the file extension with or without a dot
     */
    public static String getFileExtension(String name, boolean includeDot)
    {
        int lastIndexOf = name.lastIndexOf(".");
        
        if (lastIndexOf == -1) 
        {
            return ""; // empty extension
        }
        
        if(includeDot)
        {
            return name.substring(lastIndexOf).toLowerCase();
        }
        
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
    
    /**
     * gets the file extension from a string
     * @param name the filename / path
     * @return the file extension with a dot
     */
    public static String getFileExtension(String name)
    {
        return getFileExtension(name, true);
    }
    
    /**
     * gets the file extension from a string
     * @param name the filename / path
     * @return the file extension with a dot
     */
    public static String getFileExtension(File file)
    {
        return getFileExtension(file.getName(), true);
    }
    
    /**
     * gets the file extension from a string
     * @param name the filename / path
     * @param includeDot should the extension include a dot
     * @return the file extension with or without a dot
     */
    public static String getFileExtension(File file, boolean includeDot) 
    {
        return getFileExtension(file.getName(), includeDot);   
    }


    /**
     * Splits a tag in the form of namespace:subtag into an array of length 2 with the namespace at 0 and the subtag at 1
     * @param fullTag A tag in the form of namespace:subtag
     * @return An array of length 2 with the namespace at 0 and the subtag at 1
     */
    public static String[] partitionTag(String fullTag)
    {
        String[] split = fullTag.split(":", 2);
        String[] tag = new String[] { "", "" };

        if(split.length == 1)
        {
            tag[0] = "";
            tag[1] = split[0].strip().toLowerCase();
        }
        else
        {
            tag[0] = split[0].strip().toLowerCase();
            tag[1] = split[1].strip().toLowerCase();
        }

        return tag;
    }
}
