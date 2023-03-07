package uwu.nyaa.owo.finalproject.data;

import java.io.File;

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
}
