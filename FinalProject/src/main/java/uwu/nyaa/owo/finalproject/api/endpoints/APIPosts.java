package uwu.nyaa.owo.finalproject.api.endpoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.MultiPartFormDataParser;
import uwu.nyaa.owo.finalproject.data.PartInputStream;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TablePost;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.data.models.FileUpload;
import uwu.nyaa.owo.finalproject.data.models.HashInfoBase;
import uwu.nyaa.owo.finalproject.data.models.Post;

@Path("/posts")
public class APIPosts
{
    @Context
    private HttpServletRequest request;

    private final ObjectMapper jsonMapper = new ObjectMapper();
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBulkPostMetadata(@QueryParam("limit") int limit) throws JsonProcessingException
    {
        if(limit <= 0 || limit > 200)
        {
            limit = 200;
        }
        
        Logger.info(Integer.toString(limit));
        
        List<Post> items = TablePost.getPosts(limit);
        
        return Response.status(200)
                .entity(this.jsonMapper.writeValueAsString(items))
                .build();
    }
    
    
    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload_post() throws IOException, ServletException
    {
        final String BOUNDARY = MultiPartFormDataParser.getBoundary(request);
        final InputStream FORM_STREAM = MultiPartFormDataParser.getResetableInputStream(request.getInputStream());

        Logger.info(BOUNDARY);

        if(BOUNDARY == null)
        {
            return Response.status(400, "Bad format data").build();
        }

        PartInputStream partInputStream = MultiPartFormDataParser.readPrecedingBoundary(BOUNDARY, FORM_STREAM);

        if(partInputStream.isLastPart())
        {
            return Response.status(400, "Bad format data").build();
        }

        MultiPartFormDataParser.Part p = MultiPartFormDataParser.readNextPart(BOUNDARY, FORM_STREAM);

        if(p == null || p.qualifiers.get("name") == null || !p.qualifiers.get("name").equals("data"))
        {
            return Response.status(400, "Could not read data field from form first").build();
        }

        // partInputStream doesn't support #mark and #reset, so we need to read these here to add later
        byte[] header = p.partInputStream.readNBytes(256);
        byte mime = FileDetector.getFileMimeType(header);

        if(mime == FileFormat.UNKNOWN)
        {
            return Response.status(400, "Unknown data format").build();
        }

        String tempPath = Files.createTempDirectory("upload").toString();
        File file = Paths.get(tempPath, Long.toString(System.currentTimeMillis())).toFile();

        try(FileOutputStream fout = new FileOutputStream(file))
        {
            fout.write(header);
            p.partInputStream.transferTo(fout);
        }

        Logger.info("Saved upload to {}, exists {}", file, file.exists());

        FileUpload fa = FileProcessor.addFile(file);

        if (fa.hash_id == -1)
        {
            Logger.warn("Server fricked up file upload or something, returned a -1 hash_id");
            return Response.status(500, "Server fcked up man").build();
        }

        p = MultiPartFormDataParser.readNextPart(BOUNDARY, FORM_STREAM);

        if(p == null || p.qualifiers.get("name") == null || !p.qualifiers.get("name").equals("title"))
        {
            Logger.debug("Could not read title from file upload");
            return Response.status(400, "Could not read title field from form data").build();
        }
        
        String title = new String(p.partInputStream.readNBytes(1024), StandardCharsets.UTF_8);

        if(title == null || title.isEmpty() || title.isBlank())
        {
            Logger.debug("File upload title was blank empty or null");
            return Response.status(400, "Title was null, blank or empty, bad request").build();
        }

        if(p.partInputStream.read() != PartInputStream.EOF)
        {
            Logger.debug("File upload title was too long");
            return Response.status(400, "Title was longer than 1024 bytes, bad request").build();
        }

        p = MultiPartFormDataParser.readNextPart(BOUNDARY, FORM_STREAM);

        if(p == null || p.qualifiers.get("name") == null || !p.qualifiers.get("name").equals("description"))
        {
            Logger.debug("Could not read description from form data");
            return Response.status(400, "Could not read description field from form data").build();
        }

        String description = new String(p.partInputStream.readNBytes(5*1024), StandardCharsets.UTF_8);

        if(description == null || description.isEmpty() || description.isBlank())
        {
            Logger.debug("File upload description was empty blank or null");
            return Response.status(400, "Description was null, blank or empty, bad request").build();
        }

        if(p.partInputStream.read() != PartInputStream.EOF)
        {
            Logger.debug("File upload description was too long");
            return Response.status(400, "Description was longer than 5*1024 bytes, bad request").build();
        }

        int post_id = TablePost.insertPost(title, description, Arrays.asList(fa.hash_id));

        if(post_id == -1)
        {
            Logger.debug("Post id returned -1, somehow");
            return Response.status(500, "Could not make post, server error").build();
        }
        
        Post post = new Post(post_id, title, description);
        post.files.add(new HashInfoBase(fa));

        return Response.status(200).entity(jsonMapper.writeValueAsString(post)).build();
    }
}
