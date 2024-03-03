package ch.tbz.injection.role;

import ch.tbz.injection.authority.Authority;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;

    public Role() {
    }

    public Role(Integer id, String name, Set<Authority> authorities) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
    }
}
