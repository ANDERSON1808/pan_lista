package com.pe.pandero.asamblea;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.pe.pandero.asamblea");

        noClasses()
            .that()
            .resideInAnyPackage("com.pe.pandero.asamblea.service..")
            .or()
            .resideInAnyPackage("com.pe.pandero.asamblea.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.pe.pandero.asamblea.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
