package portal.fei.api.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResource {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("content")
    private final byte[] content;

    public FileResource(String name, String type, byte[] content){
        this.name = name;
        this.type = type;
        this.content = content;
    }
}
