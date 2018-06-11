package uk.co.optimisticpanda.app.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ValidationResult {

    private final List<String> reasons = new ArrayList<>();

    private ValidationResult(List<String> reasons ) {
        this.reasons.addAll(reasons);
    }

    public static ValidationResult success() {
        return new ValidationResult(emptyList());
    }

    public static ValidationResult failure(String... message) {
        return new ValidationResult(asList(message));
    }

    public static ValidationResult failure(List<String> messages) {
        return new ValidationResult(messages);
    }


    @JsonProperty("success")
    public boolean isSuccess() {
        return reasons.isEmpty();
    }

    @JsonProperty("reasons")
    public List<String> getReasons() {
        return reasons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return Objects.equals(reasons, that.reasons);
    }

    @Override
    public int hashCode() {

        return Objects.hash(reasons);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidationResult{");
        sb.append("reasons=").append(getReasons());
        sb.append("success=").append(isSuccess());
        sb.append('}');
        return sb.toString();
    }
}
