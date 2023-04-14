package uwu.nyaa.owo.finalproject.api.endpoints;

import java.util.LinkedList;
import java.util.List;

import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.db.TableHashTag;
import uwu.nyaa.owo.finalproject.data.db.TablePost;
import uwu.nyaa.owo.finalproject.data.db.TableTag;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;
import uwu.nyaa.owo.finalproject.data.models.Post;
import uwu.nyaa.owo.finalproject.data.models.TagFileCount;

@Path("tags")
public class APITags
{
    private final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Endpoint which returns a json list of tag objects
     * 
     * @param limit The number of tags to return
     * @return a json with the number of tags requested
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags(@QueryParam("limit") int limit)
    {
        if (limit <= 0)
        {
            limit = 200;
        }

        Logger.info(Integer.toString(limit));

        List<FullTag> items = TableTag.getTags(limit);

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in getTags while processing json");
            return Response.status(500).build();
        }
    }

    /**
     * Gets the number of files associated with the given list of tags, Should
     * specify each tag using the id with query params
     * 
     * @param tag_id The list of tag ids to get the file count of
     * @return Json with the file count for each tag
     */
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get_file_count(@QueryParam("tid") List<Integer> tag_id)
    {
        if (tag_id == null || tag_id.size() == 0)
        {
            return Response.status(400, "no tag ids given, use tid=<tag_id>").build();
        }

        List<TagFileCount> items = TableTag.getFileCount(tag_id);

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in get_file_count while processing json");
            return Response.status(500).build();
        }
    }

    /**
     * Endpoint for creating tags, should get posted a json array with the namespace
     * and the subtag to create This endpoint also acts as tag search, because it
     * returns a json array of full tag metadata, so you can search by tag names
     * here
     * 
     * @param json The json array
     * @return A list of tags created / searched
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response createTags(String json)
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

        // unholy map filter
        List<String> create = tags.stream().filter(ft -> {
            return !(ft.subtag == null || ft.namespace == null || ft.subtag.isEmpty() || ft.subtag.isBlank());
        }).map(ft -> {
            if (ft.namespace.strip().isBlank())
            {
                return ft.subtag;
            }

            return ft.namespace + ":" + ft.subtag;
        }).toList();

        if (create.size() == 0)
        {
            return Response.status(400, "Given tags was empty when parsed").build();
        }

        Logger.debug("creating tags {}", create);

        List<FullTag> items = TableTag.insertAndSelectMany(create);

        if (items.size() == 0)
        {
            Logger.debug(
                    "creating tags returned empty list, either no tags were actually given, or the server fkced up");
            return Response.status(500, "oboy, server trouble").build();
        }

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in createTags while processing json");
            return Response.status(500).build();
        }
    }

    /**
     * Gets all files with the given tags, should be a list of json with the tag ids
     * you want, it should ensure that all files returned have every single tag
     * searched
     * 
     * @param json     The list of json (FullTag object) you want, must contain at
     *                 least the tag_id
     * @param limit    The number of files you want
     * @param withTags If each file should contain a list of it's own tags
     * @return A json array of file objects
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/files")
    public Response getFilesWithTags(String json, @QueryParam("limit") int limit,
            @QueryParam("tags") @DefaultValue("true") boolean withTags)
    {
        if (limit <= 0)
        {
            limit = 200;
        }

        Logger.debug("got request for files with tags: {}\n limit: {}, with tags: {}", json, limit, withTags);

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

        int[] tag_ids = tags.stream().mapToInt(FullTag::getTagId).toArray();

        List<HashInfo> items = TableHashTag.getFilesContaining(tag_ids, limit, withTags);

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in getFilesWithTags while processing json");
            return Response.status(500).build();
        }
    }

    /**
     * Gets all posts with the given tags, should be a list of json with the tag ids
     * you want, it should ensure that all posts returned have every single tag
     * searched
     * 
     * @param json     The list of json (FullTag object) you want, must contain at
     *                 least the tag_id
     * @param limit    The number of posts you want
     * @param withTags If each post should contain a list of it's own tags
     * @return A json array of file objects
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/posts")
    public Response getPostsWithTags(String json, @QueryParam("limit") int limit,
            @QueryParam("tags") @DefaultValue("true") boolean withTags)
    {
        if (limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        Logger.debug("got request for posts with tags: {}\n limit: {}, with tags: {}", json, limit);

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

        // oya it's gamer time
        if(tags.size() == 0)
        {
            return Response.status(200).entity("[]").build();
        }

        int[] tag_ids = tags.stream().mapToInt(FullTag::getTagId).toArray();

        List<Post> items = TablePost.getPostsContaining(tag_ids, limit, withTags);

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        }
        catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in getFilesWithTags while processing json");
            return Response.status(500).build();
        }
    }
}
