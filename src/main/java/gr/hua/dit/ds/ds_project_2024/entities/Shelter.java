package gr.hua.dit.ds.ds_project_2024.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table
public class Shelter extends User {

    @NotBlank
    @Size(max = 30)
    @Column
    private String name;

    @NotBlank
    @Size(max = 20)
    @Column
    private String location;

    @NotBlank
    @Size(max = 30)
    @Column
    private String address;

    @Size(max = 200)
    @Column
    private String description;

    @Enumerated
    @Column
    private Status approvalStatus;

    @OneToMany(mappedBy = "onShelter", cascade = {CascadeType.ALL})
    private List<Pet> petsAvailable;

    @OneToMany(mappedBy = "fromShelter", cascade = {CascadeType.ALL})
    private List<Adoption> adoptionRequests;

    @OneToMany(mappedBy = "shelter", cascade = {CascadeType.ALL})
    private List<Contact> contacts;

    public Shelter() {
    }

    public Shelter(String username, String password, String email, String phone,  String name, String location, String address, String description, Status approvalStatus) {
        super(username, password, email, phone);
        this.name = name;
        this.location = location;
        this.address = address;
        this.description = description;
        this.approvalStatus = approvalStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<Pet> getPetsAvailable() {
        return petsAvailable;
    }

    public void setPetsAvailable(List<Pet> petsAvailable) {
        this.petsAvailable = petsAvailable;
    }

    public List<Adoption> getAdoptionRequests() {
        return adoptionRequests;
    }

    public void setAdoptionRequests(List<Adoption> adoptionRequests) {
        this.adoptionRequests = adoptionRequests;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}