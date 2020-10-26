package network;

import global.Utils;
import openApi.OpenApiEntity;
import openApi.OpenApiRepository;
import openApiMappings.OpenApiMappingEntity;
import openApiMappings.OpenApiMappingRepository;

public class NetworkInitializer {
  OpenApiMappingRepository openApiMappingRepository;
  OpenApiRepository openApiRepository;
  Graph graph;

/**
 * Total number of nodes
 */
  int n;
  /**
   * Represent complexity of the Network.
   * 		If C = 0, then the Network will only have one dimension and there will be only one path from Node 1 to Node N
   *  	If C = i, then every Node will have i additional edge(s) to (an)other randomly assigned node(s)
   */
  int c;
  /**
   * a boolean indicating if the Network is allowed to have loops
   */
  boolean loop;

  public NetworkInitializer(int n,int c, boolean loop) {
    this.n = n;
    this.c = c;
    this.loop = loop;
    this.openApiMappingRepository = new OpenApiMappingRepository();
    this.openApiRepository = new OpenApiRepository();
  }

  public void deleteTestEntitys() {
    this.openApiRepository.deleteAllTestApis();
    this.openApiMappingRepository.deleteAllTestMappings();
  }

  public void createNetwork() {
    this.graph = new Graph(n);
    System.out.println("Create Nodes and Horizontal Line ");

    OpenApiEntity source = new OpenApiEntity("1");
    OpenApiEntity target = new OpenApiEntity("2");
    graph.addEdge(1,2);

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


      if(i<this.n+1) {
        graph.addEdge(i - 1, i);
      }
    }

    System.out.println("Create Complexity");

    if(c>0) {
      if (loop) {
        for (int i = 1; i < n; i++) {
          for (int j = 0; j < c; j++) {
            int r = Utils.getRandomNumberInRange(1, n, i);
            source = new OpenApiEntity(i + "");
            target = new OpenApiEntity(r + "");
            OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
            OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity( target, source);
            this.openApiMappingRepository.save(openApiMappingEntity);
            this.openApiMappingRepository.save(openApiMappingEntityReversed);
            graph.addEdge(i, r);
          }
        }
      } else {
        for (int i = 1; i < n - 1; i++) {
          for (int j = 0; j < c; j++) {
            int r = Utils.getRandomNumberInRange(i, n, i);
            source = new OpenApiEntity(i + "");
            target = new OpenApiEntity(r + "");
            OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
            OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity( target, source);
            this.openApiMappingRepository.save(openApiMappingEntity);
            this.openApiMappingRepository.save(openApiMappingEntityReversed);
            graph.addEdge(i, r);
          }
        }
      }
    }
    System.out.println("Network Created");
  }

  public int getN() {
    return n;
  }

  public void setN(int n) {
    this.n = n;
  }

  public void setLoop(boolean loop) {
    this.loop = loop;
  }

  public int getC() {
    return this.c;
  }

  public void setC(int c) {
    this.c = c;
  }

  public Graph getGraph() {
    return graph;
  }
}
