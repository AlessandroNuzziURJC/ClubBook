package clubbook.backend.model;

import clubbook.backend.model.enumClasses.RoleEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * Represents a user role within the application.
 */
@Table(name = "T_Role")
@Entity
public class Role {

    /**
     * The unique identifier pf the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer roleId;

    /**
     * Name of the role. Can't be null.
     */
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    /**
     * Description of the role. Can't be null.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Date of creation of the object. Can't be modified.
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Date of update of the object in the database.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Constructs a new Role with the specified role enum.
     *
     * @param roleEnum the role enum to set the name
     */
    public Role(RoleEnum roleEnum) {
        this.name = roleEnum;
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.description = "Role " + roleEnum.toString().toLowerCase();
    }

    /**
     * Default constructor for Role.
     */
    public Role() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
