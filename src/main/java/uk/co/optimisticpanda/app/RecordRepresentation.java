package uk.co.optimisticpanda.app;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordRepresentation {

    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String price;
    @JsonProperty
    private List<String> tags;
    @JsonProperty
    private Dimension dimensions;
    @JsonProperty
    private WarehouseLocation warehouseLocation;
    @JsonProperty
    private Map<String, Object> extras = new HashMap<>();

    @JsonAnySetter
    public void extras(final String o, final String v) {
        extras.put(o, v);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RecordRepresentation{");
        sb.append("id='").append(id).append('\'');
        sb.append(",\n\t name='").append(name).append('\'');
        sb.append(",\n\t price='").append(price).append('\'');
        sb.append(",\n\t extras=").append(extras);
        sb.append(",\n\t tags=").append(tags);
        sb.append(",\n\t dimensions=").append(dimensions);
        sb.append(",\n\t wareHouseLocation=").append(warehouseLocation);
        sb.append('}');
        return sb.toString();
    }

    public static class Dimension {

        @JsonProperty
        private long length;
        @JsonProperty
        private long width;
        @JsonProperty
        public Map<String, Object> extras = new HashMap<>();

        @JsonAnySetter
        public void extras(final String o, final String v) {
            extras.put(o, v);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Dimension{");
            sb.append("length=").append(length);
            sb.append(", width=").append(width);
            sb.append(", extras=").append(extras);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class WarehouseLocation {

        @JsonProperty
        public Map<String, Object> extras = new HashMap<>();

        @JsonAnySetter
        public void extras(final String o, final String v) {
            extras.put(o, v);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WarehouseLocation{");
            sb.append("extras=").append(extras);
            sb.append('}');
            return sb.toString();
        }
    }

}
