package elevator.server;

import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

@Path("/")
public class WebResource {

    private final StartedElevatorServer server;

    public WebResource(StartedElevatorServer server) {
        this.server = server;
    }

    @POST
    @Path("/new-participant")
    public void newParticipant(@QueryParam("email") String email, @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        server.addElevatorGame(new Email(email), new URL(serverURL));
    }

    @POST
    @Path("/new-participant2")
    public void newParticipant(@QueryParam("email") Email email, @QueryParam("serverURL") URL serverURL) throws MalformedURLException {
        server.addElevatorGame(email, serverURL);
    }

    @GET
    @Path("/emails")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Email> emails() {
        return server.emails();
    }

    @GET
    @Path("{path : .*\\.js}")
    @Produces("application/javascript;charset=UTF-8")
    public File js(@PathParam("path") String path) throws URISyntaxException {
        return file(path);
    }

    @GET
    @Path("{path : .*\\.html}")
    @Produces("text/html;charset=UTF-8")
    public File html(@PathParam("path") String path) throws URISyntaxException {
        return file(path);
    }

    @GET
    @Path("{path : .*\\.png}")
    @Produces("image/png")
    public File png(@PathParam("path") String path) throws URISyntaxException {
        return file(path);
    }

    private File file(String path) throws URISyntaxException {
        if (!exists(path)) {
            throw new NotFoundException();
        }
        return new File(new File("elevator-server/src/main/webapp"), path);
    }

    private boolean exists(String path) throws URISyntaxException {
        if (path.endsWith("/")) {
            return false;
        }

        try {
            File root = new File("elevator-server/src/main/webapp");
            File file = new File(root, path);
            if (!file.exists() || !file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

}