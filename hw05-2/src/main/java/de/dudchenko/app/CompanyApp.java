package de.dudchenko.app;

import de.dudchenko.Employee;
import de.dudchenko.HerzEmployee;
import de.dudchenko.MerzEmployee;
import de.dudchenko.customannotations.Company;

import java.lang.reflect.Field;

public class CompanyApp {

    @Company(companyName = "BMW", location = "Munich")
    private static Employee employee = new Employee();

    @Company(companyName = "Mercedes", location = "Stuttgart")
    private static Employee employee2 = new Employee();

    public static void main(String[] args) throws NoSuchFieldException {

        Employee herzEmployee = new HerzEmployee();
        Employee merzEmployee = new MerzEmployee();

        Company herzEmpCompanyAnnotation = herzEmployee
                .getClass().getAnnotation(Company.class);

        System.out.println(herzEmpCompanyAnnotation.companyName());
        System.out.println(herzEmpCompanyAnnotation.location());
        System.out.println();

        Company merzEmpCompanyAnnotation = merzEmployee
                .getClass().getAnnotation(Company.class);

        System.out.println(merzEmpCompanyAnnotation.companyName());
        System.out.println(merzEmpCompanyAnnotation.location());
        System.out.println();

        Field emplField = CompanyApp.class.getDeclaredField("employee");
        Company empAnnotation = emplField.getAnnotation(Company.class);
        System.out.println(empAnnotation.companyName());
        System.out.println(empAnnotation.location());

        Company emp2Annotation = CompanyApp.class
                .getDeclaredField("employee2").getAnnotation(Company.class);
        System.out.println(emp2Annotation.companyName());
        System.out.println(emp2Annotation.location());

        System.out.println();
        System.out.println("Herz employee:");
        System.out.println(herzEmployee);

        System.out.println(merzEmployee);

        System.out.println(employee);
    }

}
