package uwu.nyaa.owo.finalproject.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

public class MultiPartFormDataParser
{
    public static class Part
    {
        public PartInputStream partInputStream;
        public Map<String, String> qualifiers;
        public String contentDisposition;
    }
    
    public static Part readNextPart(String boundary, InputStream entityStream) throws IOException
    {
        InputStream inputStream = getResetableInputStream(entityStream);
        PartInputStream partInputStream;

        do
        {
            partInputStream = new PartInputStream(inputStream, ("\r\n" + boundary).getBytes(StandardCharsets.US_ASCII));
            
            Map<String, String> contentHeaders = getContentHeader(partInputStream);

            String dispositionHeader = contentHeaders.get("content-disposition");

            if (dispositionHeader != null)
            {
                Part p = new Part();
                p.partInputStream = partInputStream;
                p.qualifiers = extractQualifiers(dispositionHeader);;
                p.contentDisposition = dispositionHeader;
                
                return p;
            }

            break;
        }
        while (!partInputStream.isLastPart());

        return null;
    }
    
    public static String getBoundary(HttpServletRequest request)
    {
        String contentTypeHeader = request.getHeader("Content-type");

        if(contentTypeHeader == null || contentTypeHeader.isEmpty())
            return null;

        Map<String, String> qualifiers = extractQualifiers(contentTypeHeader);

        String boundary = qualifiers.get("boundary");

        if(boundary == null)
            return null;

        return "--" + boundary;
    }
    public static Map<String, String> extractQualifiers(String headerValue)
    {
        Map<String, String> qualifiers = new HashMap<>();
        String[] assignments = headerValue.split(";");
        for (String assignment : assignments)
        {
            String[] lr = assignment.split("=");
            if (lr.length == 2)
            {
                qualifiers.put(lr[0].trim().toLowerCase(), lr[1].trim().replaceAll("\"", ""));
            }
        }
        return qualifiers;
    }
    
    public static InputStream getResetableInputStream(InputStream entityStream)
    {
        if (!entityStream.markSupported())
        {
            return new BufferedInputStream(entityStream);
        }
        else
        {
            return entityStream;
        }
    }
    
    public static byte[] readStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        is.transferTo(os);
        return os.toByteArray();
    }


    public static PartInputStream readPrecedingBoundary(String boundary, InputStream inputStream) throws IOException 
    {
        // PartInputStream is used to consume the first boundary leading the message
        // body. That's possible because the way PartInputStream is implemented the
        // leading boundary is a part with empty part's body.

        PartInputStream partInputStream = new PartInputStream(inputStream, boundary.getBytes(StandardCharsets.US_ASCII));

        byte[] bytes = readStream(partInputStream);
        
        if (bytes.length != 0) 
        {
            WrappedLogger.warning(String.format("Multipart form-data message should start with boundary, but begins with bytes: %s", ByteHelper.bytesToHex(bytes)));
        }
        return partInputStream;
    }
    
    public static Map<String, String> getContentHeader(PartInputStream inputStream) throws IOException
    {
        List<String> lines = readLines(inputStream);
        return lines.stream()
                .map(l -> l.split(":", 2))
                .collect(Collectors.toMap(
                        a -> a[0].trim().toLowerCase(),
                        a -> a[1].trim(),
                        (v1, v2) -> v1,    // keep first value if duplicate key found
                        LinkedHashMap::new // preserve order of header fields
                ));
//                .collect(Collectors.toMap(a -> a[0].trim().toLowerCase(), a -> a[1].trim()));
    }
    
    public static List<String> readLines(PartInputStream is) throws IOException
    {        
        List<String> lines = new ArrayList<>();
        String line = readLine(is);

        while (!line.isEmpty())
        {
            lines.add(line);
            line = readLine(is);
        }
        return lines;

    //  Uncomment the following code to use streams instead of the loop above
    //  return Stream.generate(() -> readLine(is))
//                    .takeWhile(line -> !line.isEmpty())
//                    .collect(Collectors.toList());    
    }

    public static String readLine(PartInputStream is) throws IOException
    {
        int c1 = is.read();
        if (c1 == PartInputStream.EOF)
        {
            return "";
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int c2 = is.read();
        // searching for end of file (EOF) or sequence of carriage return (CR) and line
        // feed (LF) sequence characters, whichever comes first
        while (c2 !=  PartInputStream.EOF && !(c1 ==  PartInputStream.CR && c2 ==  PartInputStream.LF))
        {
            os.write(c1);
            c1 = c2;
            c2 = is.read();
        }
        return os.toString(StandardCharsets.US_ASCII);
    }
}
