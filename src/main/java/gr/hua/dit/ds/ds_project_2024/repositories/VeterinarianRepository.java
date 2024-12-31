package gr.hua.dit.ds.ds_project_2024.repositories;

import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Integer> {
    Optional<Veterinarian> findVeterinarianByUsername(String userName);
}
