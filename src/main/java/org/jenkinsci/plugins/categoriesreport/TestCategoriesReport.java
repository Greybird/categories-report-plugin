package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Descriptor;
import hudson.model.Describable;
import org.kohsuke.stapler.DataBoundConstructor;

public class TestCategoriesReport {
    private final String filePattern;
    private final String name;
    private final String categoriesRegex;

    @DataBoundConstructor
    public TestCategoriesReport(String name, String filePattern, String categoriesRegex) {
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
}
