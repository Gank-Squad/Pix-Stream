package uwu.nyaa.owo.finalproject.api.endpoints;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;

@Path("/files")
public class APIFiles
{
    private final ObjectMapper jsonMapper = new ObjectMapper();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileMetadata(@QueryParam("limit") int limit) throws JsonProcessingException
    {
        if(limit <= 0 || limit > 200)
        {
            limit = 200;
        }
        
        WrappedLogger.info(Integer.toString(limit));
        
        List<HashInfo> items = TableFile.getFiles(limit);
        
        return Response.status(200)
                .entity(this.jsonMapper.writeValueAsString(items))
                .build();
    }
    
    
    @GET
    @Produces({ "image/png", "image/jpeg", "image/webp", "image/gif" })
    @Path("/{filehash}")
    public Response getContentFiles(@PathParam("filehash") String filehash)
    {
        if (filehash.length() != 64 || !filehash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        WrappedLogger.info(String.format("Request for file with hash: %s", filehash));

        String mediaPath = PathHelper.getMediaPath(filehash);
        File f = new File(mediaPath);

        WrappedLogger.info(String.format("Found media path: %s", f.getAbsolutePath()));

        if (!f.isFile())
        {
            return Response.status(404, "Could not find file").build();
        }

        return Response.ok(f, "image/png").build();
    }
    
    
    @GET
    @Produces({ "image/png", "image/jpeg", "image/webp", "image/gif" })
    @Path("/t/{filehash}")
    public Response getThumbnails(@PathParam("filehash") String filehash)
    {
        if (filehash.length() != 64 || !filehash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        WrappedLogger.info(String.format("Request for file with hash: %s", filehash));

        String mediaPath = PathHelper.getThmbnailPath(filehash);
        File f = new File(mediaPath);

        WrappedLogger.info(String.format("Found media path: %s", f.getAbsolutePath()));

        if (!f.isFile())
        {
            return Response.status(404, "Could not find file").build();
        }

        return Response.ok(f, "image/png").build();
    }
}
