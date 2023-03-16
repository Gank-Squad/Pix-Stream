package uwu.nyaa.owo.finalproject.api.multipart;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class MultiPartMessageProvider implements MessageBodyReader<MultiPartMessage>
{
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {

        return MultiPartMessage.class.isAssignableFrom(type)
                && mediaType.toString().toLowerCase().startsWith("multipart/form-data");
    }

    @Override
    public MultiPartMessage readFrom(Class<MultiPartMessage> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException
    {

        if (!httpHeaders.getFirst("Content-type").toLowerCase().startsWith("multipart/form-data"))
        {
            throw new IllegalArgumentException(
                    "MultiPartMessageBodyReader is applicable for multipart/form-data content type only.");
        }

        String boundary = getBoundary(httpHeaders);
        InputStream inputStream = getResetableInputStream(entityStream);
        PartInputStream partInputStream = readPrecedingBoundary(boundary, inputStream);

        MultiPartMessage multiPartMessage = new MultiPartMessage();
        while (!partInputStream.isLastPart())
        {
            partInputStream = new PartInputStream(inputStream, ("\r\n" + boundary).getBytes(StandardCharsets.US_ASCII));
            Map<String, String> contentHeaders = getContentHeader(partInputStream);
            String dispositionHeader = contentHeaders.get("content-disposition");

            Map<String, String> qualifiers;
            if (dispositionHeader != null)
            {
                qualifiers = extractQualifiers(dispositionHeader);

                if (qualifiers.get("filename") != null)
                {
                    multiPartMessage.addPart(createFilePart(qualifiers, partInputStream));
                }
                else
                {
                    multiPartMessage.addPart(createFieldPart(qualifiers, partInputStream));
                }
            }
        }
        return multiPartMessage;
    }

    private PartInputStream readPrecedingBoundary(String boundary, InputStream inputStream) throws IOException {
        // PartInputStream is used to consume the first boundary leading the message
        // body. That's possible because the way PartInputStream is implemented the
        // leading boundary is a part with empty part's body.
        PartInputStream partInputStream = new PartInputStream(inputStream,
                boundary.getBytes(StandardCharsets.US_ASCII));
        byte[] bytes = readStream(partInputStream);
        if (bytes.length != 0) 
        {
//            List<Byte> bytesList = IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).collect(toList());
//            String bytesAsHex = bytesList.stream().map(b -> String.format("%02X", Byte.toUnsignedInt(b)))
//                    .collect(Collectors.joining(" "));

            WrappedLogger.warning(String.format("Multipart form-data message should start with boundary, but begins with bytes: %s", ByteHelper.bytesToHex(bytes)));
        }
        return partInputStream;
    }

    private Part createFilePart(Map<String, String> qualifiers, InputStream partInputStream)
            throws FileNotFoundException, IOException
    {
        String tempPath = Files.createTempDirectory("upload").toString();
        File file = Paths.get(tempPath, qualifiers.get("filename")).toFile();
        partInputStream.transferTo(new FileOutputStream(file));
        return FilePart.of(qualifiers.getOrDefault("name", "unnamed"), file);
    }

    private Part createFieldPart(Map<String, String> qualifiers, InputStream partInputStream) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        partInputStream.transferTo(os);
        return FieldPart.of(qualifiers.getOrDefault("name", "unnamed"), os.toString());
    }

    private String getBoundary(MultivaluedMap<String, String> httpHeaders)
    {
        String contentTypeHeader = httpHeaders.getFirst("Content-type");
        Map<String, String> qualifiers = extractQualifiers(contentTypeHeader);
        String boundary = "--" + qualifiers.get("boundary");

        return boundary;
    }

    private byte[] readStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        is.transferTo(os);
        return os.toByteArray();
    }

    private Map<String, String> getContentHeader(PartInputStream inputStream) throws IOException
    {
        List<String> lines = readLines(inputStream);
        return lines.stream().map(l -> l.split(":"))
                .collect(Collectors.toMap(a -> a[0].trim().toLowerCase(), a -> a[1].trim()));
    }

    private List<String> readLines(PartInputStream is) throws IOException
    {
        List<String> lines = new ArrayList<>();
        String line = readLine(is);
        while (!line.isEmpty())
        {
            lines.add(line);
            line = readLine(is);
        }
        return lines;
    }

    private String readLine(PartInputStream is) throws IOException
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

    private Map<String, String> extractQualifiers(String headerValue)
    {
        Map<String, String> qualifiers = new HashMap<>();
        String[] assignments = headerValue.split(";");
        for (String assignment : assignments)
        {
            String lr[] = assignment.split("=");
            if (lr.length == 2)
            {
                qualifiers.put(lr[0].trim().toLowerCase(), lr[1].trim().replaceAll("\"", ""));
            }
        }
        return qualifiers;
    }

    private InputStream getResetableInputStream(InputStream entityStream)
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
}