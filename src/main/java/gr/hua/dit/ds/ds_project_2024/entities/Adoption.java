package gr.hua.dit.ds.ds_project_2024.entities;

import jakarta.persistence.*;

@Entity
@Table
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer id;

    @Enumerated
    @Column
    private Status status;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="pet_id")
    private Pet petToAdopt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="citizen_id")
    private Citizen applicant;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="shelter_id")
    private Shelter fromShelter;

    public Adoption() {
    }

    public Adoption(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Pet getPetToAdopt() {
        return petToAdopt;
    }

    public void setPetToAdopt(Pet petToAdopt) {
        this.petToAdopt = petToAdopt;
    }

    public Citizen getApplicant() {
        return applicant;
    }

    public void setApplicant(Citizen applicant) {
        this.applicant = applicant;
    }

    public Shelter getFromShelter() {
        return fromShelter;
    }

    public void setFromShelter(Shelter fromShelter) {
        this.fromShelter = fromShelter;
    }

    @Override
    public String toString() {
        return "Adoption Request" +
                " id=" + id +
                ", from applicant=" + applicant;
    }
}
