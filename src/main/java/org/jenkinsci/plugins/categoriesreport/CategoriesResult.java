package org.jenkinsci.plugins.categoriesreport;


import java.text.DecimalFormat;
import java.util.List;

public class CategoriesResult extends CategoryResult {

    private final List<CategoryResult> categories;
    private final DecimalFormat percentageFormat = new DecimalFormat("0%");

    public CategoriesResult(List<CategoryResult> categories) {
        super("Total");
        this.categories = categories;
        for(CategoryResult cr : categories) {
            addSuccesses(cr.getSuccesses());
            addFailures(cr.getFailures());
        }
    }

    public List<CategoryResult> getCategories() {
        return categories;
    }

    public String format(double val) {
        if (val < 1d && val > .99d) {
            return "<100%";
        }
        if (val > 0d && val < .01d) {
            return ">0%";
        }
        return percentageFormat.format(val);
    }

    public String getBackgroundColor(CategoryResult cr) {
        return cr.getFailures() > 0 ? "#E86850" : "#71E66D";
    }
}
