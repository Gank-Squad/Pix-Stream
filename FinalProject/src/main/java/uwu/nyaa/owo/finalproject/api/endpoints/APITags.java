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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags(@QueryParam("limit") int limit) 
    {
        if (limit <= 0 || limit > 200)
        {
            limit = 200;
        }

        Logger.info(Integer.toString(limit));

        List<FullTag> items = TableTag.getTags(limit);

        try {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();    
        }
        catch (JsonProcessingException e) 
        {
            Logger.error(e, "Error in getTags while processing json");
            return Response.status(500).build();
        }
    }
    
    
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get_file_count(@QueryParam("tid") List<Integer> tag_id)
    {
        if(tag_id == null || tag_id.size() == 0)
        {
            return Response.status(400,"no tag ids given, use tid=<tag_id>").build();
        }

        List<TagFileCount> items = TableTag.getFileCount(tag_id);

        try
        {
            return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
        } catch (JsonProcessingException e)
        {
            Logger.error(e, "Error in get_file_count while processing json");
            return Response.status(500).build();
        }
    }
    
    
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
        

        List<String> create = tags.stream().filter(ft -> 
        {
            return !(ft.subtag == null || ft.namespace == null || ft.subtag.isEmpty() || ft.subtag.isBlank());
        })
        .map(ft -> {
            if(ft.namespace.strip().equals(""))
            {
                return ft.subtag;
            }
            
            return ft.namespace + ":" + ft.subtag;
        }).toList();
        
        if(create.size() == 0)
        {
            return Response.status(400, "Given tags was empty when parsed").build();
        }
        
        Logger.debug("creating tags {}", create);
        
        List<FullTag> items = TableTag.insertAndSelectMany(create);

        if(items.size() == 0)
        {
            return  Response.status(500, "oboy, server trouble").build();
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


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/files")
    public Response getFilesWithTags(String json, @QueryParam("limit") int limit, @QueryParam("tags") @DefaultValue("true") boolean withTags)
    {
        if (limit <= 0 || limit > 200)
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
    
    
    
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/posts")
    public Response getPostsWithTags(String json, @QueryParam("limit") int limit, @QueryParam("tags") @DefaultValue("true") boolean withTags)
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
        
        int[] tag_ids = tags.stream().mapToInt(FullTag::getTagId).toArray();
        
        List<Post> items = TablePost.getPostsContaining(tag_ids, limit, false);
        
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
