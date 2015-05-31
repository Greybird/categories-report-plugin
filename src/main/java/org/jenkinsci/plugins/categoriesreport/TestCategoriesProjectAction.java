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
    return "lastCompletedBuild/" + super.getUrlName();
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
    while(currentBuild!=null) {
      List<TestCategoriesActionBase> actions = getTestCategoriesActions(currentBuild, name);
      if(!actions.isEmpty() && (!currentBuild.isBuilding())) {
        List<CategoryResult> categories = actions.get(0).getCategories();
        SortCategories(categories);
        return new Results(categories, lastSuccessfulBuild.number);
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
