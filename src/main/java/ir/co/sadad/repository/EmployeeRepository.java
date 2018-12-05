package ir.co.sadad.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import ir.co.sadad.domain.Employee;

public class EmployeeRepository extends AbstractRepository<Employee, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeRepository() {
        super(Employee.class);
    }

}
