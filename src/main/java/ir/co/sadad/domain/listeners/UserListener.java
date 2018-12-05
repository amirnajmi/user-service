package ir.co.sadad.domain.listeners;

import ir.co.sadad.domain.User;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.security.enterprise.SecurityContext;
import java.time.Instant;

public class UserListener {

    @Inject
    SecurityContext securityContext;


    public String getCurrentUserLogin() {
        if (securityContext == null || securityContext.getCallerPrincipal() == null) {
            return null;
        }
        return securityContext.getCallerPrincipal().getName();
    }

    @PrePersist
    void onCreate(User entity) {
        entity.setAccountId(getCurrentUserLogin());
    }

    @PreUpdate
    void onUpdate(User entity) {
        entity.setAccountId(getCurrentUserLogin());
    }


}
