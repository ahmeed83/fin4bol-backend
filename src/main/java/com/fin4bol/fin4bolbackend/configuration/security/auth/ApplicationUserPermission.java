package com.fin4bol.fin4bolbackend.configuration.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class defines the permissions that every role has.
 */
@Getter
@AllArgsConstructor
public enum ApplicationUserPermission {

    CUSTOMER_READ("customer:read"), CUSTOMER_WRITE("customer:write"), CUSTOMER_UPDATE(
            "customer:update"), CUSTOMER_DELETE("customer:delete"),

    EMPLOYEE_READ("employee:read"), EMPLOYEE_WRITE("employee:write"), EMPLOYEE_UPDATE(
            "employee:update"), EMPLOYEE_DELETE("employee:delete");

    private final String permission;
}
