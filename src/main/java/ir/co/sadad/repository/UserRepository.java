package ir.co.sadad.repository;

import javax.persistence.EntityManager;
import javax.inject.Inject;
import ir.co.sadad.domain.User;

public class UserRepository extends AbstractRepository<User, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRepository() {
        super(User.class);
    }

}
