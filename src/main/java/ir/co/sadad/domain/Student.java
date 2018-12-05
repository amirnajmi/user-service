package ir.co.sadad.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * @author ammac
 */
@Entity
public class Student extends User {

    @Basic
    private String studentNo;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

}