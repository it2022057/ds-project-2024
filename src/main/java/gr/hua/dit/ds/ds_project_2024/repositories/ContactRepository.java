package gr.hua.dit.ds.ds_project_2024.repositories;

import gr.hua.dit.ds.ds_project_2024.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByScheduledVisitBetween(LocalDateTime start, LocalDateTime end);
}
