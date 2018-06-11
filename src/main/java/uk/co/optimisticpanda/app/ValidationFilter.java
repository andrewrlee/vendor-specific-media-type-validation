package uk.co.optimisticpanda.app;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import uk.co.optimisticpanda.app.validation.RequestValidator;
import uk.co.optimisticpanda.app.validation.ValidationResult;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class ValidationFilter implements ContainerRequestFilter  {

    private final RequestValidator requestValidator = new RequestValidator();

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {

        String contentType = ctx.getHeaderString("Content-Type");

        try (  InputStreamReader reader = new InputStreamReader(ctx.getEntityStream(), Charsets.UTF_8)){
            String json = CharStreams.toString(reader);

            InputStream in = new ByteArrayInputStream(json.getBytes());
            ctx.setEntityStream(in);

            ValidationResult result = requestValidator.validate(contentType, json);

            if (!result.isSuccess()) {

                ctx.abortWith(Response.status(BAD_REQUEST)
                        .entity(String.format("{\"errors\" : [%s]}", result.getReasons().stream().collect(joining("\", \"", "\"", "\""))))
                        .build());
            }
        }
    }
}
