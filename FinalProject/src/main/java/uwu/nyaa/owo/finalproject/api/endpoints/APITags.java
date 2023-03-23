package uwu.nyaa.owo.finalproject.api.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TableHashTag;
import uwu.nyaa.owo.finalproject.data.db.TableTag;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;

import java.util.List;

@Path("tags")
public class APITags
{
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags(@QueryParam("limit") int limit) throws JsonProcessingException
    {
        if(limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        WrappedLogger.info(Integer.toString(limit));

        List<FullTag> items = TableTag.getTags(limit);

        return Response.status(200)
                .entity(this.jsonMapper.writeValueAsString(items))
                .build();
    }


    // TODO: make this a post request or something because paths for something as variable as tags is a bad idea
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{namespace}/{subtag}/files")
    public Response getTaggedFiles(@PathParam("namespace") String namespace,
                                   @PathParam("subtag") String subtag, @QueryParam("limit") int limit) throws JsonProcessingException
    {
        if(namespace == null || subtag == null)
        {
            return Response.status(400).build();
        }
        if(limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        WrappedLogger.info(Integer.toString(limit) + " " + namespace + " " + subtag);

        String tag = namespace + ":" + subtag;
        int tagId = TableTag.getTagID(tag);

        if(tagId == -1)
            return Response.status(404).build();

        List<HashInfo> items = TableHashTag.getFiles(tagId, limit, true);

        return Response.status(200)
                .entity(this.jsonMapper.writeValueAsString(items))
                .build();
    }
}
