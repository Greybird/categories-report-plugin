package org.jenkinsci.plugins.categoriesreport;

import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Recorder;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;

@SuppressWarnings({"unchecked", "unused"})
public class TestCategoriesPublisher extends Recorder {

  private static final Logger logger = Logger.getLogger(TestCategoriesPublisher.class.getName());
  private final String filePattern;
  private final String name;
  private final String categoriesRegex;

  @DataBoundConstructor
  public TestCategoriesPublisher(String name, String filePattern, String categoriesRegex) {
      this.name = name;
      this.filePattern = filePattern;
      this.categoriesRegex = categoriesRegex;
  }

  public String getName() {
      return name;
  }

  public String getFilePattern() {
    return filePattern;
  }

  public String getCategoriesRegex() {
    return categoriesRegex;
  }

  @Override
  public BuildStepMonitor getRequiredMonitorService() {
    return BuildStepMonitor.NONE;
  }

  @Override
  public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
    return Collections.singleton(new TestCategoriesProjectAction(project, name));
  }

  @Override
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
    TestCategoriesLog log = new TestCategoriesLog(listener);
    NUnitProcessor processor = new NUnitProcessor(log, build, listener, filePattern, categoriesRegex);
    Map<String, CategoryResult> results = processor.run();
    build.addAction(new TestCategoriesRunAction(name, results.values()));
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
