package ir.co.sadad.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * @author ammac
 */
@Entity
public class Employee extends User {

    @Basic
    private String employeeNo;

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

}