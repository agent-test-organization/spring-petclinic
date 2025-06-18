package org.springframework.samples.petclinic.owner;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for pet analytics features. Provides both web views and REST API endpoints
 * to test the PetAnalyticsService.
 */
@Controller
public class PetAnalyticsController {

	private final PetAnalyticsService petAnalyticsService;

	private final OwnerRepository ownerRepository;

	public PetAnalyticsController(PetAnalyticsService petAnalyticsService, OwnerRepository ownerRepository) {
		this.petAnalyticsService = petAnalyticsService;
		this.ownerRepository = ownerRepository;
	}

	/**
	 * Web page to display analytics dashboard Visit: http://localhost:8080/analytics
	 */
	@GetMapping("/analytics")
	public String showAnalyticsDashboard(Model model) {
		try {
			// Get analytics data synchronously for the web page
			CompletableFuture<Map<String, Object>> analyticsData = petAnalyticsService.analyzeAllPetsAsync();
			Map<String, Object> data = analyticsData.get();

			model.addAttribute("analytics", data);
			model.addAttribute("title", "Pet Analytics Dashboard");

			return "analytics/dashboard";
		}
		catch (Exception e) {
			model.addAttribute("error", "Failed to load analytics: " + e.getMessage());
			return "error";
		}
	}

	/**
	 * REST API endpoint for analytics data Visit: http://localhost:8080/api/analytics
	 */
	@GetMapping("/api/analytics")
	@ResponseBody
	public CompletableFuture<ResponseEntity<Map<String, Object>>> getAnalyticsData() {
		return petAnalyticsService.analyzeAllPetsAsync()
			.thenApply(data -> ResponseEntity.ok(data))
			.exceptionally(throwable -> ResponseEntity.internalServerError().build());
	}

	/**
	 * REST API endpoint for individual pet report Visit:
	 * http://localhost:8080/api/pets/{petId}/report
	 */
	@GetMapping("/api/pets/{petId}/report")
	@ResponseBody
	public ResponseEntity<String> getPetReport(@PathVariable Integer petId, @RequestParam Integer ownerId) {
		try {
			// Find the owner and pet
			var owner = ownerRepository.findById(ownerId);
			if (owner.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Pet pet = owner.get().getPet(petId);
			if (pet == null) {
				return ResponseEntity.notFound().build();
			}

			String report = petAnalyticsService.generatePetReport(pet);
			return ResponseEntity.ok(report);

		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Web page to show individual pet reports Visit:
	 * http://localhost:8080/owners/{ownerId}/pets/{petId}/analytics
	 */
	@GetMapping("/owners/{ownerId}/pets/{petId}/analytics")
	public String showPetAnalytics(@PathVariable Integer ownerId, @PathVariable Integer petId, Model model) {
		try {
			var owner = ownerRepository.findById(ownerId);
			if (owner.isEmpty()) {
				return "redirect:/owners";
			}

			Pet pet = owner.get().getPet(petId);
			if (pet == null) {
				return "redirect:/owners/" + ownerId;
			}

			String report = petAnalyticsService.generatePetReport(pet);

			model.addAttribute("owner", owner.get());
			model.addAttribute("pet", pet);
			model.addAttribute("report", report);
			model.addAttribute("title", "Pet Analytics - " + pet.getName());

			return "analytics/petReport";

		}
		catch (Exception e) {
			model.addAttribute("error", "Failed to generate pet report: " + e.getMessage());
			return "error";
		}
	}

}