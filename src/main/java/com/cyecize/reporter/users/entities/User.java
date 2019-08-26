package com.cyecize.reporter.users.entities;

import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;
import com.cyecize.summer.areas.security.interfaces.UserDetails;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_registered", nullable = false)
    private LocalDateTime dateRegistered;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.REMOVE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<GrantedAuthority> roles;

    public User() {
        this.enabled = true;
        this.roles = new ArrayList<>();
        this.dateRegistered = LocalDateTime.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDateRegistered() {
        return this.dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<GrantedAuthority> getRoles() {
        return this.roles;
    }

    public void setRoles(List<GrantedAuthority> roles) {
        this.roles = roles;
    }

    @Transactional
    public void addRole(Role role) {
        if (this.roles.stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase(role.getAuthority()))) return;
        this.roles.add(role);
    }

    @Transactional
    public void removeRole(Role role) {
        if (!this.roles.contains(role)) return;
        this.roles.remove(role);
    }
}
