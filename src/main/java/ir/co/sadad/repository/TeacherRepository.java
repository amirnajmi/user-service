package ir.co.sadad.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import ir.co.sadad.domain.Teacher;

public class TeacherRepository extends AbstractRepository<Teacher, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeacherRepository() {
        super(Teacher.class);
    }

}
