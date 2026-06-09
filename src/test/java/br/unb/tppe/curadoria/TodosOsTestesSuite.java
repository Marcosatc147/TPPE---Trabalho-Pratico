package br.unb.tppe.curadoria;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("br.unb.tppe.curadoria")
@IncludeClassNamePatterns(".*Test")
public class TodosOsTestesSuite {
}
