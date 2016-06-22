package org.jenkinsci.plugins.categoriesreport;

import junit.framework.TestCase;
import org.jenkinsci.plugins.categoriesreport.xml.CategoriesType;
import org.jenkinsci.plugins.categoriesreport.xml.CategoryType;
import org.jenkinsci.plugins.categoriesreport.xml.TestCaseType;
import org.jenkinsci.plugins.categoriesreport.xml.TestSuiteType;

import java.util.HashMap;
import java.util.Map;

public class CategoriesAggregatorTest extends TestCase {

    private final String ALL_PATTERN = ".*";
    private final String DEFAULT_CATEGORY = "Category";
    private final String FALLBACK_CATEGORY = "Fallback";

    private final String SUB_PATTERN = "Is(.*)";
    private final String SUB_MATCHING_CATEGORY_STRIPPED = "Ok";
    private final String SUB_MATCHING_CATEGORY = "Is" + SUB_MATCHING_CATEGORY_STRIPPED;
    private final String SUB_MATCHING_CATEGORY2_STRIPPED = "Ok2";
    private final String SUB_MATCHING_CATEGORY2 = "Is" + SUB_MATCHING_CATEGORY2_STRIPPED;
    private final String SUB_NOT_MATCHING_CATEGORY = "Nok";

    public void testNoContextForTestIsWorking() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(ALL_PATTERN, null, categories);

        aggregator.addTest(true);
        aggregator.addTest(false);

        assertEquals(0, categories.size());
    }

    public void testSuiteNoCategoriesAreIgnored() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(ALL_PATTERN, null, categories);

        TestSuiteType testSuite = new TestSuiteType();
        aggregator.enterTestSuite(testSuite);
        aggregator.addTest(true);

        assertEquals(0, categories.size());
    }

    public void testSuiteCategoriesAreCountedCorrectlyForSuccess() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(ALL_PATTERN, null, categories);

        TestSuiteType testSuite = new TestSuiteType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(DEFAULT_CATEGORY);
        cats.getCategory().add(cat);
        testSuite.setCategories(cats);

        aggregator.enterTestSuite(testSuite);
        aggregator.addTest(true);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(DEFAULT_CATEGORY);
        assertNotNull(result);
        assertEquals(DEFAULT_CATEGORY, result.getName());
        assertEquals(1, result.getSuccesses());
        assertEquals(0, result.getFailures());
        assertEquals(1, result.getTotal());
        assertEquals(1.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.0d, result.getFailurePercentage(), 0.01);
    }

    public void testSuiteCategoriesAreCountedCorrectlyForFailure() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(ALL_PATTERN, null, categories);

        TestSuiteType testSuite = new TestSuiteType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(DEFAULT_CATEGORY);
        cats.getCategory().add(cat);
        testSuite.setCategories(cats);

        aggregator.enterTestSuite(testSuite);
        aggregator.addTest(false);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(DEFAULT_CATEGORY);
        assertNotNull(result);
        assertEquals(DEFAULT_CATEGORY, result.getName());
        assertEquals(0, result.getSuccesses());
        assertEquals(1, result.getFailures());
        assertEquals(1, result.getTotal());
        assertEquals(0.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(1.0d, result.getFailurePercentage(), 0.01);
    }

    public void testCaseNoCategoriesAreIgnored() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, null, categories);

        TestCaseType testCase = new TestCaseType();
        aggregator.enterTestCase(testCase);
        aggregator.addTest(true);

        assertEquals(0, categories.size());
    }

    public void testCaseCategoriesAreCountedCorrectlyForSuccess() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, null, categories);

        TestCaseType testCase = new TestCaseType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(SUB_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testCase.setCategories(cats);

        aggregator.enterTestCase(testCase);
        aggregator.addTest(true);
        aggregator.exitTestCase();
        aggregator.addTest(true);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(SUB_MATCHING_CATEGORY_STRIPPED);
        assertNotNull(result);
        assertEquals(SUB_MATCHING_CATEGORY_STRIPPED, result.getName());
        assertEquals(1, result.getSuccesses());
        assertEquals(0, result.getFailures());
        assertEquals(1, result.getTotal());
        assertEquals(1.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.0d, result.getFailurePercentage(), 0.01);
    }

    public void testCaseCategoriesAreCountedCorrectlyForFailure() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, null, categories);

        TestCaseType testCase = new TestCaseType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(SUB_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testCase.setCategories(cats);

        aggregator.enterTestCase(testCase);
        aggregator.addTest(false);
        aggregator.exitTestCase();
        aggregator.addTest(false);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(SUB_MATCHING_CATEGORY_STRIPPED);
        assertNotNull(result);
        assertEquals(SUB_MATCHING_CATEGORY_STRIPPED, result.getName());
        assertEquals(0, result.getSuccesses());
        assertEquals(1, result.getFailures());
        assertEquals(1, result.getTotal());
        assertEquals(0.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(1.0d, result.getFailurePercentage(), 0.01);
    }

    public void testCaseDefaultCategoryIsCorrectlyUsedForSuccess() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, FALLBACK_CATEGORY, categories);

        TestCaseType testCase = new TestCaseType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testCase.setCategories(cats);

        aggregator.enterTestCase(testCase);
        aggregator.addTest(true);
        aggregator.exitTestCase();
        aggregator.addTest(true);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(FALLBACK_CATEGORY);
        assertNotNull(result);
        assertEquals(FALLBACK_CATEGORY, result.getName());
        assertEquals(2, result.getSuccesses());
        assertEquals(0, result.getFailures());
        assertEquals(2, result.getTotal());
        assertEquals(1.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.0d, result.getFailurePercentage(), 0.01);
    }

    public void testCaseDefaultCategoryIsCorrectlyUsedForFailure() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, FALLBACK_CATEGORY, categories);

        TestCaseType testCase = new TestCaseType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testCase.setCategories(cats);

        aggregator.enterTestCase(testCase);
        aggregator.addTest(false);
        aggregator.exitTestCase();
        aggregator.addTest(false);

        assertEquals(1, categories.size());
        CategoryResult result = categories.get(FALLBACK_CATEGORY);
        assertNotNull(result);
        assertEquals(FALLBACK_CATEGORY, result.getName());
        assertEquals(0, result.getSuccesses());
        assertEquals(2, result.getFailures());
        assertEquals(2, result.getTotal());
        assertEquals(0.0d, result.getSuccessPercentage(), 0.01);
        assertEquals(1.0d, result.getFailurePercentage(), 0.01);
    }

    public void testMixedCategoriesAreCountedCorrectlyForMixedResults() {
        Map<String, CategoryResult> categories = new HashMap<String, CategoryResult>();
        CategoriesAggregator aggregator = new CategoriesAggregator(SUB_PATTERN, null, categories);

        TestSuiteType testSuite = new TestSuiteType();
        CategoriesType cats = new CategoriesType();
        CategoryType cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        cat = new CategoryType();
        cat.setName(SUB_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testSuite.setCategories(cats);

        TestCaseType testCase1 = new TestCaseType();
        cats = new CategoriesType();
        cat = new CategoryType();
        cat.setName(SUB_MATCHING_CATEGORY2);
        cats.getCategory().add(cat);
        testCase1.setCategories(cats);

        TestCaseType testCase2 = new TestCaseType();
        cats = new CategoriesType();
        cat = new CategoryType();
        cat.setName(SUB_NOT_MATCHING_CATEGORY);
        cats.getCategory().add(cat);
        testCase2.setCategories(cats);

        aggregator.enterTestSuite(testSuite);
        aggregator.addTest(true);
        aggregator.addTest(false);
        aggregator.enterTestCase(testCase1);
        aggregator.addTest(true);
        aggregator.addTest(false);
        aggregator.exitTestCase();
        aggregator.enterTestCase(testCase2);
        aggregator.addTest(true);
        aggregator.addTest(false);
        aggregator.exitTestCase();
        aggregator.exitTestSuite();

        assertEquals(2, categories.size());
        CategoryResult result = categories.get(SUB_MATCHING_CATEGORY_STRIPPED);
        assertNotNull(result);
        assertEquals(SUB_MATCHING_CATEGORY_STRIPPED, result.getName());
        assertEquals(3, result.getSuccesses());
        assertEquals(3, result.getFailures());
        assertEquals(6, result.getTotal());
        assertEquals(0.5d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.5d, result.getFailurePercentage(), 0.01);

        result = categories.get(SUB_MATCHING_CATEGORY2_STRIPPED);
        assertNotNull(result);
        assertEquals(SUB_MATCHING_CATEGORY2_STRIPPED, result.getName());
        assertEquals(1, result.getSuccesses());
        assertEquals(1, result.getFailures());
        assertEquals(2, result.getTotal());
        assertEquals(0.5d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.5d, result.getFailurePercentage(), 0.01);
    }
}
