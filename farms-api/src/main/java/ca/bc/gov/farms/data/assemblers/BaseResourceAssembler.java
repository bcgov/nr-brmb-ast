package ca.bc.gov.farms.data.assemblers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryException;
import ca.bc.gov.brmb.common.utils.ByteUtils;
import jakarta.ws.rs.core.UriBuilder;

public abstract class BaseResourceAssembler {

    protected static URI getBaseURI() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUri();
    }

    protected static String getEtag(Object object) throws FactoryException {

        String result = null;

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdBytes = md.digest(getEntityToBytes(object));

            UUID uuid = ByteUtils.getUUID(mdBytes);

            result = uuid.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new FactoryException("Failed to create MD5 hash.", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FactoryException("Failed to serialize object.", e);
        }

        return result;
    }

    private static byte[] getEntityToBytes(Object object) throws IOException {

        byte[] result = null;

        ObjectOutput out = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            result = bos.toByteArray();

            out.close();
            out = null;

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }

        return result;
    }

    protected static <T extends BaseResource> void setSelfLink(Object id, T resource, URI baseUri) {

        String resourcePath = resource.getClass().getSimpleName();
        resourcePath = resourcePath.replace("ListRsrc", "s");
        resourcePath = resourcePath.replace("Rsrc", "s");
        resourcePath = Character.toLowerCase(resourcePath.charAt(0)) + resourcePath.substring(1);

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(resourcePath + "/{id}")
                .build(id.toString()).toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    protected static <T extends BaseResource> void setSelfLink(T resource, URI baseUri) {

        String resourcePath = resource.getClass().getSimpleName();
        resourcePath = resourcePath.replace("ListRsrc", "s");
        resourcePath = resourcePath.replace("Rsrc", "s");
        resourcePath = Character.toLowerCase(resourcePath.charAt(0)) + resourcePath.substring(1);

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(resourcePath)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }
}
