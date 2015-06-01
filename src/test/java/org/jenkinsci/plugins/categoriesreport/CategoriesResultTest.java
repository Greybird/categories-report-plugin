package org.jenkinsci.plugins.categoriesreport;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CategoriesResultTest extends TestCase {

    public void testAccumulationIsDoneCorrectly() {
        CategoryResult cr1 = new CategoryResult("1");
        cr1.addSuccesses(125);
        cr1.addFailures(562);

        CategoryResult cr2 = new CategoryResult("2");
        cr2.addSuccesses(137);
        cr2.addFailures(859);

        List<CategoryResult> cr = new ArrayList<CategoryResult>();
        cr.add(cr1);
        cr.add(cr2);

        CategoriesResult result = new CategoriesResult(cr);
        assertEquals(262, result.getSuccesses());
        assertEquals(1421, result.getFailures());
        assertEquals(1683, result.getTotal());
        assertEquals(0.16d, result.getSuccessPercentage(), 0.01);
        assertEquals(0.84d, result.getFailurePercentage(), 0.01);
    }

    public void testBackgroundColorsAreDifferentBetweenSuccessAndFailure() {
        CategoryResult successResult = new CategoryResult("1");
        successResult.addFailures(0);
        successResult.addSuccesses(562);

        CategoryResult failureResult = new CategoryResult("2");
        failureResult.addFailures(1);
        failureResult.addSuccesses(859);

        List<CategoryResult> cr = new ArrayList<CategoryResult>();
        cr.add(successResult);
        cr.add(failureResult);

        CategoriesResult result = new CategoriesResult(cr);

        String successColor = result.getBackgroundColor(successResult);
        String failureColor = result.getBackgroundColor(failureResult);

        assertNotSame(successColor, failureColor);
    }

    public void testFormatForNo() {
        List<CategoryResult> cr = new ArrayList<CategoryResult>();

        CategoriesResult result = new CategoriesResult(cr);

        assertEquals("100%", result.format(1.0d));
        assertEquals("<100%", result.format(0.999d));
        assertEquals("98%", result.format(0.98d));
        assertEquals("2%", result.format(0.02d));
        assertEquals(">0%", result.format(0.001d));
        assertEquals("0%", result.format(0d));
    }
}
