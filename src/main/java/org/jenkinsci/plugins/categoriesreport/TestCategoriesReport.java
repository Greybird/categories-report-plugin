package org.jenkinsci.plugins.categoriesreport;

import hudson.model.Descriptor;
import hudson.model.Describable;
import org.kohsuke.stapler.DataBoundConstructor;

public class TestCategoriesReport implements Describable<TestCategoriesReport> {
    private final String filePattern;
    private final String name;
    private final String categoriesRegex;
    private TestCategoriesReportDescriptor descriptor = new TestCategoriesReportDescriptor();

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

    @Override
    public Descriptor<TestCategoriesReport> getDescriptor() {
        return descriptor;
    }
}
