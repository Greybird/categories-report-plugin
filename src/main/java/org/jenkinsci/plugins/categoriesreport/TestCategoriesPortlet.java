package org.jenkinsci.plugins.categoriesreport;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.*;

public class TestCategoriesPortlet extends DashboardPortlet {

    @DataBoundConstructor
    public TestCategoriesPortlet(String name) {
        super(name);
    }

    public CategoriesResult getCategoriesResult(Collection<TopLevelItem> jobs) {
        Map<String, CategoryResult> results = new HashMap<String, CategoryResult>();
        for (TopLevelItem item : jobs) {
            if (item instanceof Job) {
                Job job = (Job) item;
                List<TestCategoriesProjectAction> projectActions = job.getActions(TestCategoriesProjectAction.class);
                if (projectActions != null) {
                    for(TestCategoriesProjectAction projectAction : projectActions) {
                        if (projectAction.getName().equals(getName())) {
                            List<CategoryResult> jobCategories = projectAction.getCategories();
                            if (jobCategories != null) {
                                merge(results, jobCategories);
                            }
                        }
                    }
                }
            }
        }
        List<CategoryResult> categories = new ArrayList<CategoryResult>(results.values());
        return new CategoriesResult(categories);
    }

    private static void merge(Map<String, CategoryResult> results, List<CategoryResult> categories) {
        for(CategoryResult cr : categories) {
            CategoryResult mappedResult = results.get(cr.getName());
            if (mappedResult == null) {
                mappedResult = new CategoryResult(cr.getName());
                results.put(mappedResult.getName(), mappedResult);
            }
            mappedResult.addFailures(cr.getFailures());
            mappedResult.addSuccesses(cr.getSuccesses());
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

        @Override
        public String getDisplayName() {
            return "Test Categories portlet";
        }
    }
}
