package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.HealthCheck;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.service.HealthCheckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("healthCheck")
public class HealthCheckController {

    private HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @RequestMapping()
    public String showHealthChecks(Model model) {
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/{id}")
    public String showHealthCheck(@PathVariable Integer id, Model model) {
        HealthCheck healthCheck = healthCheckService.getHealthCheck(id);
        model.addAttribute("healthChecks", healthCheck);
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/new")
    public String addHealthCheck(Model model) {
        HealthCheck healthCheck = new HealthCheck();
        model.addAttribute("healthCheck", healthCheck);
        return "healthCheck/healthCheck";
    }

    @PostMapping("/new")
    public String savePet(@ModelAttribute("healthCheck") HealthCheck healthCheck, Model model) {
        healthCheckService.savePet(healthCheck);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/delete/{id}")
    public String deleteHealthCheck(@PathVariable Integer id, Model model) {
        healthCheckService.deleteHealthCheck(id);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }
}
