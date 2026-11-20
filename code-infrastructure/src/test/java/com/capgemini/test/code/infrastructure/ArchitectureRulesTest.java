package com.capgemini.test.code.infrastructure;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
    packages = "com.capgemini.test.code",
    importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureRulesTest {
  @ArchTest
  static final ArchRule domainDoesNotDependOnFrameworksOrAdapters =
      noClasses().that().resideInAPackage("..domain..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("org.springframework..", "jakarta.persistence..", "feign..", "..infrastructure..");

  @ArchTest
  static final ArchRule applicationDoesNotDependOnInfrastructure =
      noClasses().that().resideInAPackage("..application..")
          .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
}
