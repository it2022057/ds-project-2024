package gr.hua.dit.ds.ds_project_2024.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @NotBlank
    @Size(max = 30)
    @Column
    private String name;

    @Column
    private Integer age;

    @NotBlank
    @Size(max = 20)
    @Column
    private String species;

    @NotBlank
    @Size(max = 10)
    @Column
    private String sex;

    @Enumerated
    @Column
    private Status approvalStatus;

    @OneToMany(mappedBy = "petExamined", cascade = {CascadeType.ALL})
    private List<HealthCheck> health;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="shelter_id")
    private Shelter onShelter;

    @OneToMany(mappedBy = "petToAdopt", cascade = {CascadeType.ALL})
    private List<Adoption> interest;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="citizen_id")
    private Citizen owner;

    @Column
    private String imagePath;

    @Transient
    private boolean loggedInVetHasNotExamined;

    public Pet() {
    }

    public Pet(String name, Integer age, String species, String sex, Status approvalStatus) {
        this.name = name;
        this.age = age;
        this.species = species;
        this.sex = sex;
        this.approvalStatus = approvalStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Status getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<HealthCheck> getHealth() {
        return health;
    }

    public void setHealth(List<HealthCheck> health) {
        this.health = health;
    }

    public Shelter getOnShelter() {
        return onShelter;
    }

    public void setOnShelter(Shelter onShelter) {
        this.onShelter = onShelter;
    }

    public List<Adoption> getInterest() {
        return interest;
    }

    public void setInterest(List<Adoption> interest) {
        this.interest = interest;
    }

    public Citizen getOwner() {
        return owner;
    }

    public void setOwner(Citizen owner) {
        this.owner = owner;
    }

    public boolean getLoggedInVetHasNotExamined() {
        return loggedInVetHasNotExamined;
    }

    public void setLoggedInVetHasNotExamined(boolean loggedInVetHasNotExamined) {
        this.loggedInVetHasNotExamined = loggedInVetHasNotExamined;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", species='" + species + '\'' +
                ", sex='" + sex + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", health=" + health +
                ", onShelter=" + onShelter +
                ", interest=" + interest +
                ", owner=" + owner +
                '}';
    }
}