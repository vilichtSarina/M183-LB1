package ch.tbz.injection.authority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "authority")
public class Authority {

    @Column(name = "id")
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    public Authority() {
    }

    public Authority(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
