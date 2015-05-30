package org.jenkinsci.plugins.categoriesreport;

import org.jenkinsci.plugins.categoriesreport.xml.CategoriesType;
import org.jenkinsci.plugins.categoriesreport.xml.TestSuiteType;
import org.jenkinsci.plugins.categoriesreport.xml.CategoryType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoriesAggregator {
  private final TestCategoriesLog logger;
  private final Map<String, CategoryResult> results;
  private final Stack<Set<String>> categoriesStack = new Stack<Set<String>>();
  private Set<String> levelCache;
  private final Pattern pattern;

  public CategoriesAggregator(TestCategoriesLog logger, String pattern, Map<String, CategoryResult> results) {
    this.logger = logger;
    this.pattern = Pattern.compile(pattern);
    this.results = results;
  }

  public void enterTestSuite(TestSuiteType testSuite) {
    enterCategories(testSuite.getCategories());
  }

  public void exitTestSuite() {
    exitCategories();
    levelCache = null;
  }

  public void addTest(boolean success) {
    ensureLevelCache();
    for(String cat : levelCache) {
      addTest(cat, success);
    }
  }

  private void enterCategories(CategoriesType categories) {
    Set<String> matchingCategories = new HashSet<String>();
    if (categories != null) {
      for(CategoryType ct : categories.getCategory()) {
        Matcher m = pattern.matcher(ct.getName());
        if (m.matches()) {
          String category = m.group(Math.min(m.groupCount(), 1));
          logger.debugConsoleLogger("Test category " + ct.getName() + " matches as " + category);
          // we add either the first capturing group (1) if present, or the match (0)
          matchingCategories.add(category);
        } else {
          logger.debugConsoleLogger("Test category " + ct.getName() + " does not match");
        }
      }
    }
    categoriesStack.push(matchingCategories);
  }

  private void exitCategories() {
    categoriesStack.pop();
  }

  private void addTest(String cat, boolean success) {
    CategoryResult result;
    if (!results.containsKey(cat)) {
      result = new CategoryResult(cat);
      results.put(cat, result);
    } else {
      result = results.get(cat);
    }
    if (success) {
      result.addSuccess();
    } else {
      result.addFailure();
    }
  }

  private void ensureLevelCache() {
    if (levelCache == null) {
      levelCache = new HashSet<String>();
      for(Set<String> cats : categoriesStack) {
        for(String cat : cats) {
          levelCache.add(cat);
        }
      }
    }
  }
}
