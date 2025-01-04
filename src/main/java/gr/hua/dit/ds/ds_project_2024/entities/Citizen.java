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
public class Citizen extends User {

    @NotBlank
    @Size(max = 30)
    @Column
    private String firstName;

    @NotBlank
    @Size(max = 30)
    @Column
    private String lastName;

    @NotBlank
    @Size(max = 30)
    @Column
    private String address;

    @OneToMany(mappedBy = "applicant", cascade = {CascadeType.ALL})
    private List<Adoption> pendingAdoptions;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Pet> adoptedPets;

    public Citizen() {
    }

    public Citizen(String username, String password, String email, String phone, String firstName, String lastName, String address) {
        super(username, password, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Adoption> getPendingAdoptions() {
        return pendingAdoptions;
    }

    public void setPendingAdoptions(List<Adoption> pendingAdoptions) {
        this.pendingAdoptions = pendingAdoptions;
    }

    public List<Pet> getAdoptedPets() {
        return adoptedPets;
    }

    public void setAdoptedPets(List<Pet> pets) {
        this.adoptedPets = pets;
    }
}