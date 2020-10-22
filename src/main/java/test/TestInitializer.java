package test;

import openApi.OpenApiEntity;
import openApi.OpenApiRepository;
import openApiMappings.OpenApiMappingEntity;
import openApiMappings.OpenApiMappingRepository;

public class TestInitializer {
  OpenApiMappingRepository openApiMappingRepository;
  OpenApiRepository openApiRepository;


  int n;

  public TestInitializer(int n) {
    this.n = n;
    this.openApiMappingRepository = new OpenApiMappingRepository();
    this.openApiRepository = new OpenApiRepository();
  }

  public void deleteTestEntitys() {
    this.openApiRepository.deleteAllTestApis();
    this.openApiMappingRepository.deleteAllTestMappings();
  }

  public void createRepositorysAndMapping() {
    OpenApiEntity source = new OpenApiEntity("1");
    OpenApiEntity target = new OpenApiEntity("2");
    for(int i =3; i< this.n + 2; i++) {
      OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
      OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity( target, source);
      OpenApiEntity savedTarget = this.openApiRepository.save(target);
      OpenApiEntity savedSource = this.openApiRepository.save(source);
      OpenApiMappingEntity savedMapping = this.openApiMappingRepository
          .save(openApiMappingEntity);
    OpenApiMappingEntity savedMapping2 = this.openApiMappingRepository
          .save(openApiMappingEntityReversed);
      source = target;
      target = new OpenApiEntity(i+"");
    }
  }

  public int getN() {
    return n;
  }

  public void setN(int n) {
    this.n = n;
  }
}
