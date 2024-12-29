package gr.hua.dit.ds.ds_project_2024.repositories;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Integer> {
    Optional<Citizen> findCitizenByUsername(String username);
}
