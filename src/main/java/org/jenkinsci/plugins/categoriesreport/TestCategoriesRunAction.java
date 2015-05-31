package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Run;
import jenkins.model.RunAction2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestCategoriesRunAction extends TestCategoriesActionBase implements RunAction2 {

  private final List<CategoryResult> categories = new ArrayList<CategoryResult>();
  private Run<?, ?> build;

  public TestCategoriesRunAction(String name, Collection<CategoryResult> results) {
    super(name);
    categories.addAll(results);
    SortCategories(categories);
  }

  public Run<?, ?> getBuild() {
    return build;
  }

  public CategoriesResult getCategoriesResult() {
    return new CategoriesResult(getCategories());
  }

  @Override
  protected List<CategoryResult> getCategoriesInternal() {
    return categories;
  }


  @Override
  public void onAttached(Run<?, ?> run) {
    build = run;
  }

  @Override
  public void onLoad(Run<?, ?> run) {
    build = run;
  }
}
