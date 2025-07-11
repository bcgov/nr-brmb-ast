package ca.bc.gov.farms.api.rest.v1.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

@Path("/import")
public interface ImportEndpoints {

    @POST
    @Path("/bpu/{fileName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response importBPU(@PathParam("fileName") String fileName, @RequestBody byte[] fileContent) throws Exception;
}
