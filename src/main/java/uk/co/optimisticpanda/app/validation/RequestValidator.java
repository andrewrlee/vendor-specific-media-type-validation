package uk.co.optimisticpanda.app.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static uk.co.optimisticpanda.app.validation.ValidationResult.failure;
import static uk.co.optimisticpanda.app.validation.ValidationResult.success;

public class RequestValidator {

    private final Map<String, Schema> schemas = ImmutableMap.of(
            "application/vnd.optimisticpanda.acme.product-v1+json", loadSchema("./product-acme-v1-schema.json"),
            "application/vnd.optimisticpanda.fizzbuzz.product-v1+json", loadSchema("./product-fizzbuzz-v1-schema.json"));


    public ValidationResult validate(final String mediaType, final String request) {

        if (!schemas.containsKey(mediaType)) {

            return failure("mediatype " + mediaType + " not recognised!");
        }

        Schema schema = schemas.get(mediaType);

        try {

            schema.validate(new JSONArray(request));
            return success();

        } catch (ValidationException e) {
            return failure(extractErrors(e));
        }
    }

    private Schema loadSchema(String schemaName) {
        try {

            String resource = Resources.toString(getResource(schemaName), UTF_8);
            JSONObject rawSchema = new JSONObject(new JSONTokener(resource));
            return SchemaLoader.load(rawSchema);

        } catch (IOException e) {
            throw new IllegalStateException("Could not load resource: " + schemaName, e);
        }
    }

    private List<String> extractErrors(ValidationException e) {

        List<String> messages = e.getCausingExceptions().stream()
                .flatMap(ex -> extractErrors(ex).stream())
                .collect(toList());

        return ImmutableList.<String>builder()
                .add(e.getMessage())
                .addAll(messages)
                .build();
    }
}
