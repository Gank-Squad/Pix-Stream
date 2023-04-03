package uwu.nyaa.owo.finalproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.tinylog.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.MultiPartFormDataParser;
import uwu.nyaa.owo.finalproject.data.PartInputStream;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;

@Path("/media")
public class APIResource
{
    /*

    terms should be a string of tags, with each tag separated by a delimiter.
    "&" or %26 will probably work. Strings passed by frontend should be encoded, but an additional check
    on backend is also probably a good idea

    Gets search results from querying the db, returns as a json with (start-end) elements
     */
    @GET
    @Produces("application/json")
    @Path("/search/{tags}/{start}-{end}")
    public Response getSearchResults(@PathParam("tags") String us_tags, @PathParam("start") String start, @PathParam("end") String end)
    {
        // I'm not really sure how to query the database, and don't have time to find out
        // Want to avoid sending like 10k things to user at once, so requests should be done in *relatively* small increments

        // all the following stuff probably doesn't matter - I know almost nothing about web security
        // check if url is encoded (by decoding and comparing)
        String s_tags;
        try
        {
            if (us_tags.equals(URLDecoder.decode(us_tags, StandardCharsets.UTF_8.toString())))
            {
                // not encoded
                s_tags = URLEncoder.encode(us_tags, StandardCharsets.UTF_8.toString());
            }
            else
            {
                // it is encoded
                s_tags = us_tags;
            }
        } catch (UnsupportedEncodingException e)
        {
            return Response.status(500).entity("{}").build();
        }

        // if it differs, encode the uri

        // with known to be encoded uri, split by delimiter
        String tags[] = s_tags.split("%26");
        // query db, store results as a json, and return

        // if theres an invalid tag, just return an empty json object
        // if start or end are invalid, probably return an error status

        return Response.status(200).entity("abc").build();
    }
}

