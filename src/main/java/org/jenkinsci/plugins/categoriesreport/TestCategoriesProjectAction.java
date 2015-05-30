package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Run;
import hudson.model.AbstractProject;
import hudson.model.AbstractBuild;

import java.util.ArrayList;
import java.util.List;

public class TestCategoriesProjectAction extends TestCategoriesActionBase {
  private final AbstractProject<?, ?> project;
  private Results results = null;

  public TestCategoriesProjectAction(AbstractProject<?, ?> project, String name) {
    super(name);
    this.project = project;
  }

  @Override
  protected List<CategoryResult> getCategoriesInternal() {
    if (results == null) {
      results = getLastActions(project, getName());
    } else {
      AbstractBuild<?,?> lastSuccessfulBuild = project.getLastSuccessfulBuild();
      if (lastSuccessfulBuild == null) {
        results = getLastActions(project, getName());
      } else if (lastSuccessfulBuild.number > results.getBuildNumber()) {
        results = getLastActions(project, getName());
      }
    }
    return results.getResults();
  }

  private Results getLastActions(AbstractProject<?, ?> project, String name) {
    final AbstractBuild<?,?> lastSuccessfulBuild = project.getLastSuccessfulBuild();
    AbstractBuild<?,?> currentBuild = project.getLastBuild();
    while(currentBuild!=null) {
      List<TestCategoriesActionBase> actions = getTestCategoriesActions(currentBuild, name);
      if(!actions.isEmpty() && (!currentBuild.isBuilding())) {
        return new Results(actions.get(0).getCategories(), lastSuccessfulBuild.number);
      }
      if(currentBuild==lastSuccessfulBuild) {
        // if even the last successful build didn't produce the test result,
        // that means we just don't have any tests configured.
        return new Results(new ArrayList<CategoryResult>(0), lastSuccessfulBuild.number);
      }
      currentBuild = currentBuild.getPreviousBuild();
    }
    return new Results(new ArrayList<CategoryResult>(0), 0);
  }

  private static List<TestCategoriesActionBase> getTestCategoriesActions(Run r, String name) {
    List<TestCategoriesActionBase> actions = new ArrayList<TestCategoriesActionBase>();
    if (r != null) {
      for(TestCategoriesActionBase a : r.getActions(TestCategoriesActionBase.class)) {
        if (a.getName().equals(name)) {
          actions.add(a);
        }
      }
    }
    return actions;
  }

  private final class Results {
    private final List<CategoryResult> results;
    private final int buildNumber;

    public Results(List<CategoryResult> results, int buildNumber) {
      super();
      this.results = results;
      this.buildNumber = buildNumber;
    }

    public List<CategoryResult> getResults() {
      return results;
    }

    public int getBuildNumber() {
      return buildNumber;
    }
  }
}
