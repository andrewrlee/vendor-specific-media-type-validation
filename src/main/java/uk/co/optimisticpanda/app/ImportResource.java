package uk.co.optimisticpanda.app;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/import")
public class ImportResource {

    public static final Logger L = LoggerFactory.getLogger(ImportResource.class);

    public ImportResource() {
    }

    @Timed
    @POST
    public ImportResultRepresentation performImport(final List<RecordRepresentation> records) {

        L.info("Received: {} records", records.size());

        return new ImportResultRepresentation(records);
    }
}