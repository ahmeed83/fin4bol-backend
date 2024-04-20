package com.fin4bol.fin4bolbackend.configuration.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Class that implement the user details.
 * isAccountNonExpired is default true.
 * isAccountNonLocked is default true.
 * isCredentialsNonExpired is default true.
 */
public class ApplicationUserPrincipal implements UserDetails {

    private final Set<? extends GrantedAuthority> grantedAuthorities;
    private final String password;
    private final String userName;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public ApplicationUserPrincipal(String userName,
                                    String password,
                                    Set<? extends GrantedAuthority> grantedAuthorities,
                                    boolean isEnabled) {
        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.userName = userName;
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
