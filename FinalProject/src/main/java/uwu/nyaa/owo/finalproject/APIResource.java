package uwu.nyaa.owo.finalproject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.api.multipart.FilePart;
import uwu.nyaa.owo.finalproject.api.multipart.MultiPartMessage;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.logging.WrappedLogger;

@Path("/media")
public class APIResource
{
    @Context
    private HttpServletRequest request;
    
    



    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload/")
    public Response postFormData(MultiPartMessage message)
    {
        // this MultiPartMessage thing i stole is a pain in the ass
        // it downloads the whole file and all the content posted without checking anything
        // but hey, it works 
        message.getParts().forEach(part -> {
           
            if(part instanceof FilePart)
            {
                FilePart fPart = (FilePart)part;
                
                FileProcessor.addFile(fPart.getFile());
            }
        });

        System.out.println(message);
        return Response.status(200).build();
    }
    

    @POST
    @Path("/upload2")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces
    public Response upload(@FormParam("file") File file)
    {
        WrappedLogger.info(file.toString());
        WrappedLogger.info(file.getAbsolutePath());

        FileProcessor.addFile(file);

        return Response.status(200).build();
    }
    
    
    @GET
    @Path("/files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiles()
    {
        
        
        return Response.status(200).build();
    }
}










