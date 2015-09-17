package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Run;
import jenkins.model.RunAction2;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestCategoriesRunAction extends TestCategoriesActionBase implements RunAction2 {

  private List<CategoryResult> categories = new ArrayList<CategoryResult>();

  private transient Run<?, ?> build;

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

  public void doDynamic(StaplerRequest request, StaplerResponse response) throws IOException, ServletException {
    String restOfPath = request.getRestOfPath();
    if (restOfPath == null || restOfPath.length() <= 1) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    String name = restOfPath.substring(1);
    TestCategoriesRunAction action = getRunAction(name);
    if (action == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    request.getView(action, "report.jelly").forward(request, response);
  }

  private TestCategoriesRunAction getRunAction(String name) {
    if (name.equals(getName())) {
      return this;
    }
    List<TestCategoriesRunAction> actions = getBuild().getActions(TestCategoriesRunAction.class);
    if (actions != null) {
      for(TestCategoriesRunAction action : actions) {
        if (action.getName().equals(name)) {
         return action;
        }
      }
    }
    return null;
  }
}
