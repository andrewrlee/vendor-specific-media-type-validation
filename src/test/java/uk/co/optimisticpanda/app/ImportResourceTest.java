package uk.co.optimisticpanda.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static javax.ws.rs.client.Entity.entity;
import static org.assertj.core.api.Assertions.assertThat;

public class ImportResourceTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(new ValidationFilter())
            .addResource(new ImportResource())
            .build();

    @Test
    public void acmeSuccess() throws Exception {

        String expected = fixture("./responses/response-acme-product-v1.json");
        String request = fixture("./requests/request-acme-product-v1.json");

        Response response = resources.client().target("/import")

                .request()
                .post(entity(request, "application/vnd.optimisticpanda.acme.product-v1+json"));

        String responseEntity = response.readEntity(String.class);


        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(MAPPER.readValue(responseEntity, JsonNode.class)).isEqualTo(MAPPER.readValue(expected, JsonNode.class));
    }


    @Test
    public void fizzbuzzSuccess() throws Exception {

        String expected = fixture("./responses/response-fizzbuzz-product-v1.json");
        String request = fixture("./requests/request-fizzbuzz-product-v1.json");

        Response response = resources.client().target("/import")

                .request()
                .post(entity(request, "application/vnd.optimisticpanda.fizzbuzz.product-v1+json"));

        String responseEntity = response.readEntity(String.class);


        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(MAPPER.readValue(responseEntity, JsonNode.class)).isEqualTo(MAPPER.readValue(expected, JsonNode.class));
    }


    @Test
    public void schemaMismatch() throws Exception {

        String expected = fixture("./responses/response-failure.json");
        String request = fixture("./requests/request-fizzbuzz-product-v1.json");

        Response response = resources.client().target("/import")

                .request()
                .post(entity(request, "application/vnd.optimisticpanda.acme.product-v1+json"));

        String responseEntity = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(MAPPER.readValue(responseEntity, JsonNode.class)).isEqualTo(MAPPER.readValue(expected, JsonNode.class));
    }

}
