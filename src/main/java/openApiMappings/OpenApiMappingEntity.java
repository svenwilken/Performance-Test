package openApiMappings;

import openApi.OpenApiEntity;

public class OpenApiMappingEntity {

  String id;
  OpenApiEntity source;
  OpenApiEntity target;
  public OpenApiMappingEntity(OpenApiEntity source, OpenApiEntity target) {
    this.source = source;
    this.target = target;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OpenApiEntity getSource() {
    return source;
  }

  public void setSource(OpenApiEntity source) {
    this.source = source;
  }

  public OpenApiEntity getTarget() {
    return target;
  }

  public void setTarget(OpenApiEntity target) {
    this.target = target;
  }
}
