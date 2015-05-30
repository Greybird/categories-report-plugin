package org.jenkinsci.plugins.categoriesreport;

import org.jenkins.ui.icon.IconType;

import org.jenkins.ui.icon.Icon;
import org.jenkins.ui.icon.IconSet;
import hudson.Plugin;

public class TestCategoriesPlugin extends Plugin {

  @Override
  public void postInitialize() throws Exception {
    IconSet.icons.addIcon(new Icon("icon-categories icon-sm", "categories-report/images/16x16/categories.png", Icon.ICON_SMALL_STYLE, IconType.PLUGIN));
    IconSet.icons.addIcon(new Icon("icon-categories icon-md", "categories-report/images/24x24/categories.png", Icon.ICON_MEDIUM_STYLE, IconType.PLUGIN));
    IconSet.icons.addIcon(new Icon("icon-categories icon-lg", "categories-report/images/32x32/categories.png", Icon.ICON_LARGE_STYLE, IconType.PLUGIN));
    IconSet.icons.addIcon(new Icon("icon-categories icon-xlg", "categories-report/images/48x48/categories.png", Icon.ICON_XLARGE_STYLE, IconType.PLUGIN));
  }


}
