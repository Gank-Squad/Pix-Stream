package uwu.nyaa.owo.finalproject.api.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
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


}
