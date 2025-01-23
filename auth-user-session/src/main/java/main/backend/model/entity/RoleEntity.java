package main.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import main.backend.model.RoleName;
import org.springframework.security.core.GrantedAuthority;

// ROLE角色是一种授权的类型(Authority权限)
@Entity
@Table(name = "t_role")
public class RoleEntity implements GrantedAuthority {

    @Id
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName name;

    // 返回特定的Authority授权名称(枚举类型字面名称)
    @Override
    public String getAuthority() {
        return name.name();
    }

    @JsonIgnore
    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
