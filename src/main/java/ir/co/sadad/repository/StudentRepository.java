package ir.co.sadad.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import ir.co.sadad.domain.Student;

public class StudentRepository extends AbstractRepository<Student, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentRepository() {
        super(Student.class);
    }

}
