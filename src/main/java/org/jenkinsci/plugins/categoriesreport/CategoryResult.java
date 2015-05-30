package org.jenkinsci.plugins.categoriesreport;

import java.io.Serializable;

public class CategoryResult implements Serializable {
  private static final long serialVersionUID = -9201212776479584290L;

  private String name;
  private int failures;
  private int successes;

  public CategoryResult(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSuccesses() {
    return successes;
  }

  public int getFailures() {
    return failures;
  }

  public int getTotal() {
    return successes + failures;
  }

  public void addSuccess() {
    successes++;
  }

  public void addFailure() {
    failures++;
  }



}
