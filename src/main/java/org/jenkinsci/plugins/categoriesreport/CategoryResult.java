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

  public double getSuccessPercentage() {
    return getTotal() != 0 ? ((double) successes / getTotal()) : 0d;
  }

  public double getFailurePercentage() {
    return getTotal() != 0 ? ((double) failures / getTotal()) : 0d;
  }

  public void addSuccess() {
    addSuccesses(1);
  }

  public void addFailure() {
    addFailures(1);
  }

  protected void addSuccesses(int count) {
    successes += count;
  }

  protected void addFailures(int count) {
    failures += count;
  }


}
