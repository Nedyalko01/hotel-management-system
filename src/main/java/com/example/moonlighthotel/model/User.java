package com.example.moonlighthotel.model;


import com.example.moonlighthotel.model.car.CarTransfer;
import com.example.moonlighthotel.model.table.TableReservation;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.*;

import static com.example.moonlighthotel.constant.UserConstant.ROLE_PREFIX;
import static com.example.moonlighthotel.constant.ValidationConstant.INVALID_EMAIL;
import static com.example.moonlighthotel.constant.ValidationConstant.INVALID_PHONE_SIZE;

@Builder
@Entity
@Table(name = "users", indexes = @Index(name = "user_name_index", columnList = "firstName, lastName"))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 255)
    private String firstName;

    @Size(min = 2, max = 255)
    private String lastName;

    @Column(unique = true)
    @Email(message = INVALID_EMAIL)
    @Size(min = 5, max = 255)
    private String email;

    @Column(unique = true)
    @Size(max = 15, message = INVALID_PHONE_SIZE)
    private String phoneNumber;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
//    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    private Set<Role> roles = new HashSet<>();

    private Instant createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RoomReservation> reservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TableReservation> tableReservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CarTransfer> transfers;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String phoneNumber, String password, Set<Role> roles,
                Instant createdAt, List<RoomReservation> reservations, List<TableReservation> tableReservations,
                List<CarTransfer> transfers) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.createdAt = createdAt;
        this.reservations = reservations;
        this.tableReservations = tableReservations;
        this.transfers = transfers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<RoomReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<RoomReservation> reservations) {
        this.reservations = reservations;
    }

    public List<TableReservation> getTableReservations() {
        return tableReservations;
    }

    public void setTableReservations(List<TableReservation> tableReservations) {
        this.tableReservations = tableReservations;
    }

    public List<CarTransfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<CarTransfer> transfers) {
        this.transfers = transfers;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            String name = role.getAuthority().toUpperCase();
            if (!name.startsWith(ROLE_PREFIX)) {
                name = ROLE_PREFIX + name;
            }
            authorities.add(new SimpleGrantedAuthority(name));
        }

        return authorities;
    }

    public Set<String> getAuthorityName() {
        Set<String> authorities = new HashSet<>();
        for (Role role : roles) {
            String name = role.getAuthority().toUpperCase();

            authorities.add(name);
        }

        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
