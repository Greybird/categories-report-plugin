package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Descriptor;
import hudson.model.Describable;
import org.kohsuke.stapler.DataBoundConstructor;

public class TestCategoriesReport {
    private final String filePattern;
    private final String name;
    private final String categoriesRegex;
    private final String defaultCategory;
    private final boolean useAlternatePercentages;

    @DataBoundConstructor
    public TestCategoriesReport(String name, String filePattern, String categoriesRegex, String defaultCategory, boolean useAlternatePercentages) {
        this.name = name;
        this.filePattern = filePattern;
        this.categoriesRegex = categoriesRegex;
        this.defaultCategory = defaultCategory;
        this.useAlternatePercentages = useAlternatePercentages;
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

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public boolean isUseAlternatePercentages() {
        return useAlternatePercentages;
    }
}
