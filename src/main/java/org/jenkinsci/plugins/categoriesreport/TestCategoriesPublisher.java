package org.jenkinsci.plugins.categoriesreport;

import hudson.model.*;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.Launcher;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Recorder;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;

@SuppressWarnings({"unchecked", "unused"})
public class TestCategoriesPublisher extends Recorder {

  private static final Logger logger = Logger.getLogger(TestCategoriesPublisher.class.getName());
  private List<TestCategoriesReport> reports = new ArrayList<TestCategoriesReport>();

  @DataBoundConstructor
  public TestCategoriesPublisher(List<TestCategoriesReport> element) {
    if (element != null) {
      this.reports = element;
    }
  }

  @SuppressWarnings("unused")
  public List<TestCategoriesReport> getReports() {
    return reports;
  }

  @Override
  public BuildStepMonitor getRequiredMonitorService() {
    return BuildStepMonitor.NONE;
  }

  @Override
  public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
    List<Action> actions = new ArrayList<Action>();
    if (reports != null) {
      for (TestCategoriesReport report : reports) {
        actions.add(new TestCategoriesProjectAction(project, report.getName()));
      }
    }
    return actions;
  }

  @Override
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
    TestCategoriesLog log = new TestCategoriesLog(listener);
    log.infoConsoleLogger("Starting processing");
    if (reports != null) {
      for(TestCategoriesReport report : reports) {
        log.infoConsoleLogger("Starting processing report " + report.getName());
        NUnitProcessor processor = new NUnitProcessor(log, build, listener, report.getFilePattern(), report.getCategoriesRegex(), report.getDefaultCategory());
        Map<String, CategoryResult> results = processor.run();
        build.addAction(new TestCategoriesRunAction(report.getName(), results.values()));
        log.infoConsoleLogger("Ended processing report " + report.getName());
      }
    }
    log.infoConsoleLogger("Ended processing");
    return true;
  }

  @Extension
  public static final class TestCategoriesPublisherDescriptor extends BuildStepDescriptor<Publisher> {

    public TestCategoriesPublisherDescriptor() {
      super(TestCategoriesPublisher.class);
      load();
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> type) {
      return true;
    }

    @Override
    public String getDisplayName() {
      return Messages.testCategories_PublisherName();
    }
  }
}
