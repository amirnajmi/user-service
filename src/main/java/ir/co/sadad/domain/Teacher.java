package ir.co.sadad.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * @author ammac
 */
@Entity
public class Teacher extends User {

    @Basic
    private String teacherNo;

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

}