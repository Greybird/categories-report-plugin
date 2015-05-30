package org.jenkinsci.plugins.categoriesreport;

import org.jenkinsci.plugins.categoriesreport.xml.ResultsType;
import org.jenkinsci.plugins.categoriesreport.xml.TestSuiteType;
import org.jenkinsci.plugins.categoriesreport.xml.TestCaseType;
import org.jenkinsci.plugins.categoriesreport.xml.ResultType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import hudson.remoting.VirtualChannel;
import org.jenkinsci.remoting.RoleChecker;
import hudson.FilePath;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NUnitProcessor {
  private final AbstractBuild<?, ?> build;
  private final BuildListener listener;
  private final String filePattern;
  private final String categoriesRegex;
  private final TestCategoriesLog logger;

  public NUnitProcessor(TestCategoriesLog logger, AbstractBuild<?, ?> build, BuildListener listener, String filePattern, String categoriesRegex) {
    this.build = build;
    this.listener = listener;
    this.filePattern = filePattern;
    this.categoriesRegex = categoriesRegex;
    this.logger = logger;
  }

  public Map<String, CategoryResult> run() throws IOException, InterruptedException {
    NUnitLocalProcessor processor = new NUnitLocalProcessor();
    processor.load(logger, getExpandedResolvedPattern(build, listener, filePattern), categoriesRegex);
    return build.getWorkspace().act(processor);
  }

  private static String getExpandedResolvedPattern(AbstractBuild<?, ?> build, BuildListener listener, String filePattern) throws IOException, InterruptedException {
    String newExpandedPattern = filePattern;
    newExpandedPattern = newExpandedPattern.replaceAll("[\t\r\n]+", " ");
    return Util.replaceMacro(newExpandedPattern, build.getEnvironment(listener));
  }

  private static class NUnitLocalProcessor implements FilePath.FileCallable<Map<String, CategoryResult>>, Serializable {

    private static final long serialVersionUID = -6221760231811109559L;

    private TestCategoriesLog logger;
    private String filePattern;
    private String categoriesPattern;

    public void load(TestCategoriesLog logger, String filePattern, String categoriesPattern) {
      this.logger = logger;
      this.filePattern = filePattern;
      this.categoriesPattern = categoriesPattern;
    }

    @Override
    public void checkRoles(RoleChecker roleChecker) throws SecurityException {
    }

    @Override
    public Map<String, CategoryResult> invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
      try {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        for(String file : getFiles(f)) {
          logger.infoConsoleLogger("Processing " + file);
           appendFile(new File(f, file), categories);
        }
        return categories;
      } catch (JAXBException e) {
        throw new IOException(e);
      }
    }


    private void appendFile(File file, Map<String, CategoryResult> categories) throws JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(ResultType.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      JAXBElement<ResultType> ts = jaxbUnmarshaller.unmarshal(new StreamSource(file), ResultType.class);
      process(ts.getValue(), categories);
    }

    private void process(ResultType testResult, Map<String, CategoryResult> categories) {
      CategoriesAggregator aggregator = new CategoriesAggregator(logger, categoriesPattern, categories);
      TestSuiteType testSuite = testResult.getTestSuite();
      if (testSuite != null) {
        processTestSuite(testSuite, aggregator);
      }
    }

    private void processTestSuite(TestSuiteType testSuite, CategoriesAggregator aggregator) {
      aggregator.enterTestSuite(testSuite);
      processResults(testSuite.getResults(), aggregator);
      aggregator.exitTestSuite();
    }

    private void processTestCase(TestCaseType tc, CategoriesAggregator aggregator) {
      if (tc.getExecuted().equalsIgnoreCase("True")) {
        boolean success = tc.getSuccess().equalsIgnoreCase("True");
        aggregator.addTest(success);
      }
    }

    private void processResults(ResultsType results, CategoriesAggregator aggregator) {
      for(TestSuiteType ts : results.getTestSuite()) {
        processTestSuite(ts, aggregator);
      }
      for(TestCaseType tc : results.getTestCase()) {
        processTestCase(tc, aggregator);
      }
    }

    private List<String> getFiles(File parentPath) {
      FileSet fs = Util.createFileSet(parentPath, filePattern);
      DirectoryScanner ds = fs.getDirectoryScanner();
      String[] files = ds.getIncludedFiles();
      return Arrays.asList(files);
    }
  }
}
