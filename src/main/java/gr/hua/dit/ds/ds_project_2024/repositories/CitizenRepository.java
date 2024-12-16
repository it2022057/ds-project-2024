package gr.hua.dit.ds.ds_project_2024.repositories;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Integer> {
}
