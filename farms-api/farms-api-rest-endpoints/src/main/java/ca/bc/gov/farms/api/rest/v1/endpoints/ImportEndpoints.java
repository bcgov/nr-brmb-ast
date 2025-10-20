package ca.bc.gov.farms.api.rest.v1.endpoints;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

@Path("/import")
public interface ImportEndpoints {

    @POST
    @Path("/bpu/{fileName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response importBPU(@PathParam("fileName") String fileName, @RequestBody byte[] fileContent) throws Exception;

    @POST
    @Path("/cra/{fileName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response importCRA(@PathParam("fileName") String fileName, @RequestBody byte[] fileContent) throws Exception;

    @POST
    @Path("/fmv/{fileName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response importFMV(@PathParam("fileName") String fileName, @RequestBody byte[] fileContent) throws Exception;

    @POST
    @Path("/ivpr/{fileName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response importIVPR(@PathParam("fileName") String fileName, @RequestBody byte[] fileContent) throws Exception;
}
