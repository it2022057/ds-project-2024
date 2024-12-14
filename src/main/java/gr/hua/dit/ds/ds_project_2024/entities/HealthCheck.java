package gr.hua.dit.ds.ds_project_2024.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class HealthCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_id")
    private Integer id;

    @Size(max = 100)
    @Column
    private String details;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="pet_id")
    private Pet petExamined;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="veterinarian_id")
    private Veterinarian byVeterinarian;

    public HealthCheck() {
    }

    public HealthCheck(String details) {
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Pet getPetExamined() {
        return petExamined;
    }

    public void setPetExamined(Pet petExamined) {
        this.petExamined = petExamined;
    }

    public Veterinarian getByVeterinarian() {
        return byVeterinarian;
    }

    public void setByVeterinarian(Veterinarian byVeterinarian) {
        this.byVeterinarian = byVeterinarian;
    }
}