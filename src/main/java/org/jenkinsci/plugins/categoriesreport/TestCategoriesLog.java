package org.jenkinsci.plugins.categoriesreport;

import hudson.model.BuildListener;

import java.io.Serializable;

class TestCategoriesLog implements Serializable {

  private BuildListener buildListener;

  public TestCategoriesLog(BuildListener buildListener) {
    super();
    this.buildListener = buildListener;
  }

  /**
   * Log an info output to the console logger
   *
   * @param message The message to be outputted
   */
  public void infoConsoleLogger(String message) {
      buildListener.getLogger().println("[categories-report] [INFO] - " + message);
  }

  /**
   * Log an error output to the console logger
   *
   * @param message The message to be outputted
   */
  public void errorConsoleLogger(String message) {
      buildListener.getLogger().println("[categories-report] [ERROR] - " + message);
  }

  /**
   * Log a warning output to the console logger
   *
   * @param message The message to be outputted
   */
  public void warningConsoleLogger(String message) {
      buildListener.getLogger().println("[categories-report] [WARNING] - " + message);
  }

}
