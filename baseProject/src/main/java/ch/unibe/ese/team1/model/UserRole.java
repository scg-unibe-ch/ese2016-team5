package ch.unibe.ese.team1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Desribes the role a of user concerning Spring security. For example, a user
 * can be a normal user or also an administrator.
 * Doesn't have a Test because there are only get and set methods.
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
