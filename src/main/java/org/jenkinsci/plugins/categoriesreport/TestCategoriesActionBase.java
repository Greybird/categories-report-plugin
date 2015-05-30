package org.jenkinsci.plugins.categoriesreport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

  protected void SortCategories(List<CategoryResult> categories) {
    Collections.sort(categories, new Comparator<CategoryResult>() {
      @Override
      public int compare(CategoryResult x, CategoryResult y) {
        int s = Integer.compare(y.getFailures(), x.getFailures());
        if (s != 0) {
          return s;
        }
        s = x.getName().compareTo(y.getName());
        return s;
      }
    });
  }

  protected abstract List<CategoryResult> getCategoriesInternal();
}
