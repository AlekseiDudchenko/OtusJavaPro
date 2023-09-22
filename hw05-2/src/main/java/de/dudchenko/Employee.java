package de.dudchenko;

import de.dudchenko.customannotations.Company;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {

    String name;
    int age;
    String company;
    String location;

    public Employee() {
        getAnnotatedInfo();
    }

    private void getAnnotatedInfo() {
        Company companyClassAnnotation = this.getClass().getAnnotation(Company.class);

        if (companyClassAnnotation != null) {
            company = companyClassAnnotation.companyName();
            location = companyClassAnnotation.location();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Employee {").append("\n")
                .append("  Name: ").append(name).append(",\n")
                .append("  Age: ").append(age).append(",\n")
                .append("  Company: ").append(company).append(",\n")
                .append("  Location: ").append(location).append("\n")
                .append("}");

        return sb.toString();
    }
}
