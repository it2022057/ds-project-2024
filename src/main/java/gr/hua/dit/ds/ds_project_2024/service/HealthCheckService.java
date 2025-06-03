package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.repositories.HealthCheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckService {

    private HealthCheckRepository healthCheckRepository;

    public HealthCheckService(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    @Transactional
    public List<HealthCheck> getHealthChecks() { return healthCheckRepository.findAll(); }

    @Transactional
    public HealthCheck getHealthCheck(Integer healthCheckId) { return healthCheckRepository.findById(healthCheckId).get(); }

    @Transactional
    public void saveHealthCheck(HealthCheck healthCheck) { healthCheckRepository.save(healthCheck); }

    @Transactional
    public void deleteHealthCheck(Integer healthCheckId) { healthCheckRepository.deleteById(healthCheckId); }

    @Transactional
    public Integer updateHealthCheck(HealthCheck healthCheck) {
        healthCheck = healthCheckRepository.save(healthCheck);
        return healthCheck.getId();
    }
}
