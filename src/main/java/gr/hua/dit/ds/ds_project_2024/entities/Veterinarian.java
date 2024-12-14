package gr.hua.dit.ds.ds_project_2024.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "firstName"),
        @UniqueConstraint(columnNames = "lastName")
})
public class Veterinarian extends User {

    @NotBlank
    @Size(max = 20)
    @Column
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column
    private String lastName;

    @OneToMany(mappedBy = "veterinarian", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
    CascadeType.DETACH, CascadeType.REFRESH})
    private List<HealthCheck> healthTests;

    public Veterinarian() {
    }

    public Veterinarian(String username, String email, String password, String firstName, String lastName) {
        super(username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
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

    public List<HealthCheck> getHealthTests() {
        return healthTests;
    }

    public void setHealthTests(List<HealthCheck> healthTests) {
        this.healthTests = healthTests;
    }
}