package org.jenkinsci.plugins.categoriesreport;

import hudson.Extension;
import hudson.model.Descriptor;

@Extension
public class TestCategoriesReportDescriptor extends Descriptor<TestCategoriesReport> {

    public TestCategoriesReportDescriptor() {
        super(TestCategoriesReport.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return Messages.testCategories_ReportName();
    }
}