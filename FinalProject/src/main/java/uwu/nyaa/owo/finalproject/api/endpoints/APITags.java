package uwu.nyaa.owo.finalproject.api.endpoints;

import java.util.List;

import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.db.TableHashTag;
import uwu.nyaa.owo.finalproject.data.db.TableTag;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;

@Path("tags")
public class APITags
{
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags(@QueryParam("limit") int limit) throws JsonProcessingException
    {
        if (limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        Logger.info(Integer.toString(limit));

        List<FullTag> items = TableTag.getTags(limit);

        return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/files")
    public Response getFilesWithTags(String json)
    {
        List<FullTag> tags;
        try
        {
            TypeFactory typeFactory = jsonMapper.getTypeFactory();
            tags = jsonMapper.readValue(json, typeFactory.constructCollectionType(List.class, FullTag.class));
        }
        catch (RuntimeException e)
        {
            Logger.error(e);
            return Response.status(500).build();
        }
        catch (JsonMappingException e)
        {
            Logger.warn(e, "Failed to map json in getFilesWithTags, ignoring");
            return Response.status(400).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.warn(e, "Failed to process json in getFilesWithTags, ignoring");
            return Response.status(400).build();
        }
        
        tags.forEach(x -> {
            Logger.info(x);
        });

        return Response.status(200).build();
    }

    // TODO: make this a post request or something because paths for something as
    // variable as tags is a bad idea
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{namespace}/{subtag}/files")
    public Response getTaggedFiles(@PathParam("namespace") String namespace, @PathParam("subtag") String subtag,
            @QueryParam("limit") int limit) throws JsonProcessingException
    {
        if (namespace == null || subtag == null)
        {
            return Response.status(400).build();
        }
        if (limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        Logger.info(Integer.toString(limit) + " " + namespace + " " + subtag);

        String tag = namespace + ":" + subtag;
        int tagId = TableTag.getTagID(tag);

        if (tagId == -1)
            return Response.status(404).build();

        List<HashInfo> items = TableHashTag.getFiles(tagId, limit, true);

        return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
    }
}
