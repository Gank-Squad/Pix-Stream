package uwu.nyaa.owo.finalproject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

import javax.print.attribute.standard.Media;

@Path("/media")
public class APIResource
{
    @GET
    @Produces({ "image/png", "image/jpeg", "image/webp", "image/gif" })
    @Path("/image/{filehash}")
    public Response getFile(@PathParam("filehash") String filehash)
    {
        if (filehash.length() != 64 || !filehash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }
        
        WrappedLogger.info(String.format("Request for file with hash: %s", filehash));
        
        String mediaPath = PathHelper.getMediaPath(filehash);
        File f = new File(mediaPath);
        
        WrappedLogger.info(String.format("Found media path: %s", f.getAbsolutePath()));
        
        if(!f.isFile())
        {
            return Response.status(404, "Could not find file").build();
        }

        return Response.ok(f, "image/png").build();
    }


    // this works, but the input stream contains a form body with data in it, and idk how to parse it properly
    // or what the correct way of doing this is
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/upload")
    public Response uploadImage(

                    InputStream uploadedInputStream, @HeaderParam("Content-Type") String fileType,
            @HeaderParam("Content-Length") long fileSize) throws IOException
    {



        // Make sure the file is not larger than the maximum allowed size.
//        if (fileSize > 1024 * 1024 * 4)
//        {
//            throw new WebApplicationException(
//                    Response.status(Response.Status.BAD_REQUEST).entity("Image is larger than " + 4 + "MB").build());
//        }

        // Generate a random file name based on the current time.
        // This probably isn't 100% safe but works fine for this example.
        String fileName = "C:/Users/alice/Home-Sync/2023Winter/SoftwareSystems/Assignments/w23-csci2020u-project-team16/tests/" + System.currentTimeMillis();

        if (fileType.equals("image/jpeg"))
        {
            fileName += ".txt";
        }
        else
        {
            fileName += ".txt";
        }

        WrappedLogger.info("File upload happening");
        WrappedLogger.info(fileName);
        // Copy the file to its location.
        Files.copy(uploadedInputStream,new File(fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Return a 201 Created response with the appropriate Location header.
        return Response.status(Response.Status.CREATED).location(URI.create("/" + fileName)).build();
    }


}