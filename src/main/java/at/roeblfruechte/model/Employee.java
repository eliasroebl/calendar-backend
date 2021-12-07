package at.roeblfruechte.model;

import at.roeblfruechte.contracts.ICopyable;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEE", schema = "CALENDAR")
public class Employee extends PanacheEntityBase implements ICopyable<Employee>, Serializable {

    @Id
    @SequenceGenerator(
            name ="employeeSequence",
            sequenceName = "employee_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "employeeSequence"
    )
    public Long id;

    @Column(name = "NAME")
    public String name;

    @Override
    public void CopyProperties(Employee other) {
        this.name = other.name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
