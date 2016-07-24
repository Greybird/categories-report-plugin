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

    public String format(double val, boolean alternateFormat) {
        if (val < 1d && val > .99d) {
            return alternateFormat
                ? ">99%"
                :"<100%";
        }
        if (val > 0d && val < .01d) {
            return alternateFormat
                ? "<1%"
                : ">0%";
        }
        return percentageFormat.format(val);
    }

    public String getForegroundColor(CategoryResult cr) {
        return cr.getFailures() > 0 ? "#7C0000" : "#007F00";
    }
}
