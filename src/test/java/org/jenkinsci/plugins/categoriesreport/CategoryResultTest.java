package org.jenkinsci.plugins.categoriesreport;

import junit.framework.TestCase;

public class CategoryResultTest extends TestCase {

    public void testCountsAreCorrect() {
        CategoryResult result = new CategoryResult("test");
        assertEquals(0, result.getSuccesses());
        assertEquals(0, result.getFailures());
        assertEquals(0, result.getTotal());
        assertEquals(0d, result.getSuccessPercentage(), 0.01);
        assertEquals(0d, result.getFailurePercentage(), 0.01);

        result.addFailures(2);
        assertEquals(0, result.getSuccesses());
        assertEquals(2, result.getFailures());
        assertEquals(2, result.getTotal());
        assertEquals(0d, result.getSuccessPercentage(), 0.01);
        assertEquals(1d, result.getFailurePercentage(), 0.01);

        result.addSuccesses(4);
        assertEquals(4, result.getSuccesses());
        assertEquals(2, result.getFailures());
        assertEquals(6, result.getTotal());
        assertEquals(0.66d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.33d, result.getFailurePercentage(), 0.01);

        result.addFailure();
        assertEquals(4, result.getSuccesses());
        assertEquals(3, result.getFailures());
        assertEquals(7, result.getTotal());
        assertEquals(0.57d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.43d, result.getFailurePercentage(), 0.01);

        result.addSuccess();
        assertEquals(5, result.getSuccesses());
        assertEquals(3, result.getFailures());
        assertEquals(8, result.getTotal());
        assertEquals(0.62d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.38d, result.getFailurePercentage(), 0.01);
    }
}
