package com.example.demo.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="TBL_EMPLOYEES")
public class EmployeeEntity {
 
    @Id
    @GeneratedValue
    private Long id;
     
    @Column(name="first_name")
    private String firstName;
     
    @Column(name="last_name")
    private String lastName;
     
    @Column(name="email", nullable=false, length=200)
    private String email;
     
    //Setters and getters
 
    @Override
    public String toString() {
        return "EmployeeEntity [id=" + id + ", firstName=" + firstName + 
                ", lastName=" + lastName + ", email=" + email   + "]";
    }
}
