package network;

import global.Utils;
import openApi.model.OpenApiEntity;
import openApi.repository.OpenApiRepository;
import openApiMappings.model.OpenApiMappingEntity;
import openApiMappings.repository.OpenApiMappingRepository;

public class NetworkInitializer {

  OpenApiMappingRepository openApiMappingRepository;
  OpenApiRepository openApiRepository;
  Graph graph;

  /**
   * Total number of nodes
   */
  int n;
  /**
   * Represent complexity of the Network. If C = 0, then the Network will only
   * have one dimension and there will be only one path from Node 1 to Node N If C
   * = i, then every Node could have up to i additional edge(s) to (an)other
   * randomly assigned node(s)
   */
  int c;
  /**
   * Represent complexity of the Network. If C_lowerBound = C, then every Node
   * will i additional edge(s) to (an)other randomly assigned node(s) If
   * C_lowerBound < C, then every Node will have in a randomly assigned number r
   * of additional edge(s) to (an)other randomly assigned node(s). r will be in
   * between C_lowerBound and C. If C_lowerBound > C, this case will be treated as
   * C_lowerBound = C
   */
  int c_lowerBound;
  /**
   * a boolean indicating if the Network is allowed to have loops
   */
  boolean loop;

  public NetworkInitializer(int n, int c, int c_lowerBound, boolean loop) {
    this.n = n;
    this.c = c;
    this.c_lowerBound = c_lowerBound;
    this.loop = loop;
    this.openApiMappingRepository = OpenApiMappingRepository.getInstance();
    this.openApiRepository = OpenApiRepository.getInstance();
  }

  public void deleteTestEntitys() {
    this.openApiRepository.deleteAllTestApis();
    this.openApiMappingRepository.deleteAllTestMappings();
  }

  public void createNetwork() {
    this.graph = new Graph(n, this);
    System.out.println("Create Nodes and Horizontal Line ");

    OpenApiEntity source = new OpenApiEntity("1");
    OpenApiEntity target = new OpenApiEntity("2");
    graph.addEdge(1, 2);
    this.openApiRepository.save(source);

    for (int i = 3; i < this.n + 2; i++) {
      OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
      OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity(target, source);
      this.openApiRepository.save(target);
      this.openApiMappingRepository.save(openApiMappingEntity);
      this.openApiMappingRepository.save(openApiMappingEntityReversed);

      source = target;
      target = new OpenApiEntity(i + "");

      if (i < this.n + 1) {
        graph.addEdge(i - 1, i);
      }
    }

    System.out.println("Create Complexity");

    if (c > 0) {
      if (loop) {
        for (int i = 1; i < n; i++) {
          int numberOfExtraNodes = getNumberOfExtraNodes();
          for (int j = 0; j < numberOfExtraNodes; j++) {
            int r = Utils.getRandomNumberInRange(1, n, i);
            source = new OpenApiEntity(i + "");
            target = new OpenApiEntity(r + "");
            OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
            OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity(target, source);
            this.openApiMappingRepository.save(openApiMappingEntity);
            this.openApiMappingRepository.save(openApiMappingEntityReversed);
            graph.addEdge(i, r);
          }
        }
      } else {
        for (int i = 1; i < n - 1; i++) {
          int numberOfExtraNodes = getNumberOfExtraNodes();
          for (int j = 0; j < numberOfExtraNodes; j++) {
            int r = Utils.getRandomNumberInRange(i, n, i);
            source = new OpenApiEntity(i + "");
            target = new OpenApiEntity(r + "");
            OpenApiMappingEntity openApiMappingEntity = new OpenApiMappingEntity(source, target);
            OpenApiMappingEntity openApiMappingEntityReversed = new OpenApiMappingEntity(target, source);
            this.openApiMappingRepository.save(openApiMappingEntity);
            this.openApiMappingRepository.save(openApiMappingEntityReversed);
            graph.addEdge(i, r);
          }
        }
      }
    }
    this.openApiRepository.commitBulkOperation();
    this.openApiMappingRepository.commitBulkOperation();
  
    System.out.println("Network Created");
  }

  private int getNumberOfExtraNodes() {
    int numberOfExtraNodes;
    if (c_lowerBound < c) {
      numberOfExtraNodes = Utils.getRandomNumberInRange(c_lowerBound, c);
    } else {
      numberOfExtraNodes = c;
    }
    return numberOfExtraNodes;
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

  public boolean isLoop() {
    return this.loop;
  }

  public int getC() {
    return this.c;
  }

  public void setC(int c) {
    this.c = c;
  }

  public int getC_lowerBound() {
    return c_lowerBound;
  }

  public void setC_lowerBound(int c_lowerBound) {
    this.c_lowerBound = c_lowerBound;
  }

  public Graph getGraph() {
    return graph;
  }

  public void setCalcTime(int value) {
    graph.setCalcTime(value);
  }

}
