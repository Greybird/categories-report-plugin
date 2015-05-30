package org.jenkinsci.plugins.categoriesreport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestCategoriesRunAction extends TestCategoriesActionBase {

  private final List<CategoryResult> categories = new ArrayList<CategoryResult>();

  public TestCategoriesRunAction(String name, Collection<CategoryResult> results) {
    super(name);
    categories.addAll(results);
    SortCategories(categories);
  }

  @Override
  protected List<CategoryResult> getCategoriesInternal() {
    return categories;
  }
}
