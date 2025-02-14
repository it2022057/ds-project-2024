package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Contact;
import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public List<Contact> getContacts() { return contactRepository.findAll(); }

    @Transactional
    public Contact getContact(Integer contactId) { return contactRepository.findById(contactId).orElse(null); }

    @Transactional
    public void saveContact(Contact contact, Citizen citizen, Shelter shelter) {
        contact.setStatus(Status.PENDING);
        contact.setCitizen(citizen);
        contact.setShelter(shelter);

        contactRepository.save(contact);
    }

    @Transactional
    public void updateContact(Contact contact){
        contactRepository.save(contact);
    }

    @Transactional
    public void deleteContact(Integer contactId) { contactRepository.deleteById(contactId); }
}
