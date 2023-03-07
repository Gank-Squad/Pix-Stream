package uwu.nyaa.owo.finalproject;

import java.io.File;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("data")
public class APIData
{

    @GET
    @Path("/img3")
    @Produces({"image/jpg", "image/png", "image/gif", "image/webp"})
    public Response getFullImage(@PathParam("path") String path) 
    {

        File file = new File("img/3.jpg");
        return Response.ok(file, "image/jpg").header("Inline", "filename=\"" + file.getName() + "\"")
                .build();
    }
    
}
