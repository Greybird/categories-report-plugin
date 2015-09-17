package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Job;
import hudson.model.Run;
import java.util.ArrayList;
import java.util.List;

public class TestCategoriesProjectAction extends TestCategoriesActionBase {
  private Results results = null;
  private Job<?, ?> job;

  public TestCategoriesProjectAction(Job<?,?> job, String name) {
    super(name);
    this.job = job;
  }

  @Override
  public String getUrlName() {
    return null;
  }

  @Override
  protected List<CategoryResult> getCategoriesInternal() {
    if (results == null) {
      results = getLastActions(job, getName());
    } else {
      Run lastSuccessfulBuild = job.getLastSuccessfulBuild();
      if (lastSuccessfulBuild == null) {
        results = getLastActions(job, getName());
      } else if (lastSuccessfulBuild.number > results.getBuildNumber()) {
        results = getLastActions(job, getName());
      }
    }
    return results.getResults();
  }

  private Results getLastActions(Job<?, ?> job, String name) {
    final Run lastSuccessfulBuild = job.getLastSuccessfulBuild();
    Run currentBuild = job.getLastBuild();

    List<CategoryResult> categories = new ArrayList<CategoryResult>(0);
    int buildNumber = lastSuccessfulBuild == null ? 0 : lastSuccessfulBuild.number;

    while(currentBuild!=null) {
      List<TestCategoriesActionBase> actions = getTestCategoriesActions(currentBuild, name);
      if(!actions.isEmpty() && (!currentBuild.isBuilding())) {
        categories = actions.get(0).getCategories();
        SortCategories(categories);
        break;
      }
      if(currentBuild==lastSuccessfulBuild) {
        // if even the last successful build didn't produce the test result,
        // that means we just don't have any tests configured.
        break;
      }
      currentBuild = currentBuild.getPreviousBuild();
    }
    return new Results(categories, buildNumber);
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
