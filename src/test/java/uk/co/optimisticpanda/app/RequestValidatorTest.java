package uk.co.optimisticpanda.app;

import com.google.common.io.Resources;
import org.junit.Test;
import uk.co.optimisticpanda.app.validation.RequestValidator;

import static com.google.common.base.Charsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.optimisticpanda.app.validation.ValidationResult.failure;
import static uk.co.optimisticpanda.app.validation.ValidationResult.success;

public class RequestValidatorTest {

    private final RequestValidator requestValidator = new RequestValidator();

    @Test
    public void checkSuccessfulValidation() throws Exception {

        String resource = Resources.toString(Resources.getResource("./sample-acme-product.json"), UTF_8);

        assertThat(

                requestValidator.validate("application/vnd.optimisticpanda.acme.product-v1+json", resource))

                .isEqualTo(success());
    }

    @Test
    public void checkValidationFailed() throws Exception {

        String resource = Resources.toString(Resources.getResource("./sample-acme-product.json"), UTF_8);

        assertThat(

                requestValidator.validate("application/vnd.optimisticpanda.fizzbuzz.product-v1+json", resource))

                .isEqualTo(failure(
                        "#/0: 5 schema violations found",
                        "#/0: required key [count] not found",
                        "#/0: required key [origin] not found",
                        "#/0: extraneous key [catalog_id] is not permitted",
                        "#/0: extraneous key [date_produced] is not permitted",
                        "#/0/dimensions: required key [weight] not found"
                ));
    }

    @Test
    public void checkMediaTypeNotRecognised() throws Exception {

        String resource = Resources.toString(Resources.getResource("./sample-acme-product.json"), UTF_8);

        assertThat(

                requestValidator.validate("application/vnd.optimisticpanda.blah.blah.product-v1+json", resource))

                .isEqualTo(failure(
                        "mediatype application/vnd.optimisticpanda.blah.blah.product-v1+json not recognised!"
                ));
    }
}