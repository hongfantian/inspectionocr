package jp.dki.labtestvaluesgraph;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "userId",
        "inspectId",
        "inspectValue",
        "inspectDate",
        "saveDate"
})
public class JsonPojo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("inspectId")
    private String inspectId;
    @JsonProperty("inspectValue")
    private String inspectValue;
    @JsonProperty("inspectDate")
    private String inspectDate;
    @JsonProperty("saveDate")
    private String saveDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("inspectId")
    public String getInspectId() {
        return inspectId;
    }

    @JsonProperty("inspectId")
    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
    }

    @JsonProperty("inspectValue")
    public String getInspectValue() {
        return inspectValue;
    }

    @JsonProperty("inspectValue")
    public void setInspectValue(String inspectValue) {
        this.inspectValue = inspectValue;
    }

    @JsonProperty("inspectDate")
    public String getInspectDate() {
        return inspectDate;
    }

    @JsonProperty("inspectDate")
    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }

    @JsonProperty("saveDate")
    public String getSaveDate() {
        return saveDate;
    }

    @JsonProperty("saveDate")
    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
