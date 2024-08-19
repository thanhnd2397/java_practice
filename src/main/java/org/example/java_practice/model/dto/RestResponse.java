package org.example.java_practice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8249914988976386010L;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("errors")
    private List<ErrorModel> error;

    @JsonProperty("data")
    private Map<String, Object> data;

    public RestResponse(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Add an attribute.
     *
     * @param key   String
     * @param value Object
     */
    public void addAttribute(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Add all attributes.
     *
     * @param attrs Map of (String, Object)
     */
    public void addAttributes(Map<String, Object> attrs) {
        data.putAll(attrs);
    }
}
