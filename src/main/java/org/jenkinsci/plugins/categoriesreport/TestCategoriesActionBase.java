package org.jenkinsci.plugins.categoriesreport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import hudson.model.Action;

public abstract class TestCategoriesActionBase implements Action {

  private String name;

  public TestCategoriesActionBase(String name) {
    this.name = name;
  }

  public String getIconFileName() {
    return getHasFailingCategories()
      ? "/plugin/categories-report/images/24x24/categories.png"
      : null;
  }

  public String getDisplayName() {
    return getName();
  }

  public String getUrlName() {
    return "testCategories";
  }

  public String getName() {
    return name;
  }

  public boolean getHasCategories() {
    return getCategories().size() > 0;
  }

  public List<CategoryResult> getCategories() {
    return getCategoriesInternal();
  }

  public boolean getHasFailingCategories() {
    for(CategoryResult cr : getCategories()) {
      if (cr.getFailures() > 0) {
        return true;
      }
    }
    return false;
  }

  public List<CategoryResult> getFailingCategories() {
    List<CategoryResult> cat = new ArrayList<CategoryResult>();
    for(CategoryResult cr : getCategories()) {
      if (cr.getFailures() > 0) {
        cat.add(cr);
      }
    }
    return cat;
  }

  protected abstract List<CategoryResult> getCategoriesInternal();
}
