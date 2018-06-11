package uk.co.optimisticpanda.app;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImportResultRepresentation {

    private List<RecordRepresentation> records;

    public ImportResultRepresentation(List<RecordRepresentation> records) {
        this.records = records;
    }

    @JsonProperty
    public long getCount() {
        return records.size();
    }

    @JsonProperty("records")
    public List<RecordRepresentation> getRecords() {
        return records;
    }
}