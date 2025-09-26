package ca.bc.gov.srm.farm.chefs.resource.preflight;

import java.util.List;

public class PreflightFormResource {

  private String id;
  private String name;
  private String snake;
  private String description;
  private List<String> idpHints;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSnake() {
    return snake;
  }

  public void setSnake(String snake) {
    this.snake = snake;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getIdpHints() {
    return idpHints;
  }

  public void setIdpHints(List<String> idpHints) {
    this.idpHints = idpHints;
  }

  @Override
  public String toString() {
    return "PreflightFormResource [id=" + id + ", name=" + name + ", snake=" + snake + ", description=" + description + ", idpHints=" + idpHints
        + "]";
  }
}
