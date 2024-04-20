package com.fin4bol.fin4bolbackend.configuration.security.auth;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.CUSTOMER_DELETE;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.CUSTOMER_READ;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.CUSTOMER_UPDATE;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.CUSTOMER_WRITE;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.EMPLOYEE_DELETE;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.EMPLOYEE_READ;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.EMPLOYEE_UPDATE;
import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserPermission.EMPLOYEE_WRITE;

/**
 * Class that defines the roles of the application. Admin, Employee and Customer.
 */
@Getter
public enum ApplicationUserRole {

    ADMIN(Set.copyOf(adminPermissions())),
    EMPLOYEE(Set.copyOf(employeePermissions())),
    CUSTOMER(Set.copyOf(customerPermissions()));

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ROLE_EMPLOYEE = "hasRole('ROLE_EMPLOYEE')";
    public static final String HAS_ROLE_ADMIN_AND_EMPLOYEE = "hasAnyRole('ROLE_ADMIN, ROLE_EMPLOYEE')";
    public static final String HAS_ROLE_CUSTOMER = "hasRole('ROLE_CUSTOMER')";

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    private static List<ApplicationUserPermission> adminPermissions() {
        List<ApplicationUserPermission> permissions = new ArrayList<>();
        permissions.add(EMPLOYEE_READ);
        permissions.add(EMPLOYEE_WRITE);
        permissions.add(EMPLOYEE_UPDATE);
        permissions.add(EMPLOYEE_DELETE);

        permissions.add(CUSTOMER_READ);
        permissions.add(CUSTOMER_WRITE);
        permissions.add(CUSTOMER_UPDATE);
        permissions.add(CUSTOMER_DELETE);
        return permissions;
    }

    private static List<ApplicationUserPermission> employeePermissions() {
        List<ApplicationUserPermission> permissions = new ArrayList<>();
        permissions.add(EMPLOYEE_READ);
        permissions.add(EMPLOYEE_WRITE);
        permissions.add(EMPLOYEE_UPDATE);
        permissions.add(EMPLOYEE_DELETE);
        return permissions;
    }


    private static List<ApplicationUserPermission> customerPermissions() {
        List<ApplicationUserPermission> permissions = new ArrayList<>();
        permissions.add(CUSTOMER_READ);
        permissions.add(CUSTOMER_WRITE);
        permissions.add(CUSTOMER_UPDATE);
        permissions.add(CUSTOMER_DELETE);
        return permissions;
    }

    /**
     * Generate granted authority for the user.
     *
     * @return set of permissions.
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        final Set<SimpleGrantedAuthority> permissionSet = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissionSet.add(new SimpleGrantedAuthority("ROLE_" + this.getRole()));
        return permissionSet;
    }

    /**
     * Get role name.
     *
     * @return role name.
     */
    public String getRole() {
        return this.name();
    }
}
