package fr.apside.formation.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 880838924596031512L;

    private Long id;

    private String lastname;

    private String firstname;

    private LocalDate birth;

    public User() {
    }

    public User(String lastname, String firstname, LocalDate birth) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birth = birth;
    }

    public User(String lastname, String firstname) {
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getLastname(), user.getLastname()) &&
                Objects.equals(getFirstname(), user.getFirstname()) &&
                Objects.equals(getBirth(), user.getBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLastname(), getFirstname(), getBirth());
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birth=" + birth +
                '}';
    }
}
