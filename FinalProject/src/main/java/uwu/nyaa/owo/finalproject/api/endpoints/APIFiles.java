package uwu.nyaa.owo.finalproject.api.endpoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uwu.nyaa.owo.finalproject.data.ByteHelper;
import uwu.nyaa.owo.finalproject.data.FileProcessor;
import uwu.nyaa.owo.finalproject.data.MultiPartFormDataParser;
import uwu.nyaa.owo.finalproject.data.PartInputStream;
import uwu.nyaa.owo.finalproject.data.PathHelper;
import uwu.nyaa.owo.finalproject.data.db.TableFile;
import uwu.nyaa.owo.finalproject.data.db.TableHash;
import uwu.nyaa.owo.finalproject.data.db.TableHashTag;
import uwu.nyaa.owo.finalproject.data.db.TablePost;
import uwu.nyaa.owo.finalproject.data.filedetection.FileDetector;
import uwu.nyaa.owo.finalproject.data.filedetection.FileFormat;
import uwu.nyaa.owo.finalproject.data.models.HashInfoBase;
import uwu.nyaa.owo.finalproject.data.models.FileUpload;
import uwu.nyaa.owo.finalproject.data.models.FullTag;
import uwu.nyaa.owo.finalproject.data.models.HashInfo;
import uwu.nyaa.owo.finalproject.data.models.Post;

@Path("/files")
public class APIFiles
{
    /**
     * This is needed for the upload endpoint
     */
    @Context
    private HttpServletRequest request;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Gets a list of files from the database
     * 
     * @param limit    The amount you want
     * @param withTags If they should have tags
     * @return a Json array of file metadata
     * @throws JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBulkFileMetadata(@QueryParam("limit") int limit, 
            @QueryParam("tags") boolean withTags,
            @QueryParam("sort") int sortBy)
            throws JsonProcessingException
    {
        if (limit <= 0)
        {
            limit = 200;
        }

        Logger.info(Integer.toString(limit));

        List<HashInfo> items = TableFile.getFiles(limit, withTags);

        return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
    }

    @GET
    @Produces({ "image/png", "image/jpeg", "image/webp", "image/gif", "application/vnd.apple.mpegurl" })
    @Path("/{filehash}")
    public Response getContentFiles(@PathParam("filehash") String filehash)
    {
        if (filehash.length() != 64 || !filehash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        Logger.info("Request for file with hash: {}", filehash);

        String mediaPath = PathHelper.getMediaPath(filehash);
        File f = new File(mediaPath);

        Logger.info("Found media path: {}", f.getAbsolutePath());

        if (f.isFile())
        {
            return Response.ok(f, "image/png").build();
        }
        else if (f.isDirectory())
        {
            File m3u8 = new File(f, "index.m3u8");

            if (m3u8.isFile())
            {
                // TODO: read the m3u8 file, and replace all occurance of {FMT} with the server
                // address
                // Pointing to the endpoint for video files below
                // I.E http://localhost:xzy/

                return Response.ok(m3u8, "application/vnd.apple.mpegurl").build();
            }
        }
        return Response.status(404, "Could not find file").build();

    }

    @GET
    @Produces({ "video/ts", "video/mp4" })
    @Path("/{filehash}/{video_fragment}")
    public Response getContentVideoFiles(@PathParam("filehash") String filehash,
            @PathParam("video_fragment") String video)
    {
        if (filehash.length() != 64 || !filehash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        if (video.length() <= 3 || !video.matches("^[0-9]+\\.ts$"))
        {
            return Response.status(400, "Bad request, video fragment should be in the form 0000.ts").build();
        }

        Logger.info("Request for file with hash: {}", filehash);

        String mediaPath = PathHelper.getMediaPath(filehash);
        File f = new File(mediaPath, video);

        Logger.info("Found media path: {}", f.getAbsolutePath());

        if (!f.isFile())
        {
            return Response.status(404, "Could not find file").build();
        }

        return Response.ok(f, "video/ts").build();
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

        Logger.info("Request for file with hash: {}", filehash);

        String mediaPath = PathHelper.getThmbnailPath(filehash);
        File f = new File(mediaPath);

        Logger.info("Found media path: {}", f.getAbsolutePath());

        if (!f.isFile())
        {
            return Response.status(404, "Could not find file").build();
        }

        return Response.ok(f, "image/jpeg").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{filehash}/info")
    public Response getFileMetadata(@PathParam("filehash") String fileHash) throws JsonProcessingException
    {
        if (fileHash.length() != 64 || !fileHash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        byte[] sha256 = ByteHelper.bytesFromHex(fileHash);
        HashInfo items = TableFile.getFile(sha256, true);

        if (items == null)
        {
            return Response.status(404, "Could not find metadata").build();
        }

        return Response.status(200).entity(this.jsonMapper.writeValueAsString(items)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{filehash}/tag")
    public Response addTagsToFile(String json, @PathParam("filehash") String fileHash)
    {
        if (fileHash.length() != 64 || !fileHash.matches("^[a-fA-F0-9]+$"))
        {
            return Response.status(400, "Bad request, must be SHA256").build();
        }

        byte[] sha256 = ByteHelper.bytesFromHex(fileHash);
        int hash_id = TableHash.getHashID(sha256);

        if (hash_id == -1)
        {
            return Response.status(404, "The file does not exist").build();
        }

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

        if (tags == null)
        {
            Logger.error("Tags was null after no error while reading json??");
            return Response.status(500).build();
        }

        for (FullTag t : tags)
        {
            if (t.tag_id == -1)
            {
                continue;
            }
            TableHashTag.insertAssociation(hash_id, t.tag_id);
        }

        return Response.status(200).build();
    }

    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload_files() throws IOException, ServletException
    {
        final String BOUNDARY = MultiPartFormDataParser.getBoundary(request);
        final InputStream FORM_STREAM = MultiPartFormDataParser.getResetableInputStream(request.getInputStream());

        Logger.info(BOUNDARY);

        if (BOUNDARY == null)
        {
            return Response.status(400, "Bad format data").build();
        }

        PartInputStream partInputStream = MultiPartFormDataParser.readPrecedingBoundary(BOUNDARY, FORM_STREAM);

        if (partInputStream.isLastPart())
        {
            return Response.status(400, "Bad format data").build();
        }

        MultiPartFormDataParser.Part p = MultiPartFormDataParser.readNextPart(BOUNDARY, FORM_STREAM);

        if (p == null || p.qualifiers.get("name") == null || !p.qualifiers.get("name").equals("data"))
        {
            return Response.status(400, "Could not read 'data' field from form data first").build();
        }

        // partInputStream doesn't support #mark and #reset, so we need to read these
        // here to add later
        byte[] header = p.partInputStream.readNBytes(256);
        byte mime = FileDetector.getFileMimeType(header);

        Logger.debug("Detected mime type: {} [{}]", mime, FileFormat.getMimeType(mime));

        if (mime == FileFormat.UNKNOWN)
        {
            return Response.status(400, "Unknown data format").build();
        }

        String tempPath = Files.createTempDirectory("upload").toString();
        File file = Paths.get(tempPath, Long.toString(System.currentTimeMillis())).toFile();

        try (FileOutputStream fout = new FileOutputStream(file))
        {
            fout.write(header);
            p.partInputStream.transferTo(fout);
        }

        Logger.info("Saved upload to {}, exists {}", file, file.exists());

        FileUpload fa = FileProcessor.addFile(file);

        if (!fa.accepted)
        {
            return Response.status(500).entity(jsonMapper.writeValueAsString(fa)).build();
        }

        return Response.status(200).entity(jsonMapper.writeValueAsString(fa)).build();
    }

}
