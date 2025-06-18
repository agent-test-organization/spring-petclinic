package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class PetAnalyticsService {

	private final OwnerRepository ownerRepository;

	private final ExecutorService executorService;

	public PetAnalyticsService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public String generatePetReport(Pet pet) {
		StringBuilder report = new StringBuilder();

		report.append("Pet Report for: ").append(pet.getName()).append("\n");
		report.append("Type: ").append(pet.getType().getName()).append("\n");
		report.append("Birth Date: ").append(formatDate(pet.getBirthDate())).append("\n");

		String petCategory = categorizeByType(pet.getType().getName());
		report.append("Category: ").append(petCategory).append("\n");

		if (pet.getVisits() != null && !pet.getVisits().isEmpty()) {
			report.append("Total Visits: ").append(pet.getVisits().size()).append("\n");
			report.append("Last Visit: ").append(getLastVisitDate(pet)).append("\n");
		}
		else {
			report.append("No visits recorded\n");
		}

		return report.toString();
	}

	private String categorizeByType(String petType) {
		return switch (petType.toLowerCase()) {
			case "dog" -> "Canine";
			case "cat" -> "Feline";
			case "bird" -> "Avian";
			case "hamster", "rabbit" -> "Small Mammal";
			default -> "Other";
		};
	}

	private String formatDate(LocalDate date) {
		return date != null ? date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "Unknown";
	}

	private String getLastVisitDate(Pet pet) {
		if (pet.getVisits() == null || pet.getVisits().isEmpty()) {
			return "No visits";
		}

		return pet.getVisits()
			.stream()
			.max(Comparator.comparing(Visit::getDate))
			.map(visit -> formatDate(visit.getDate()))
			.orElse("No visits");
	}

	public CompletableFuture<Map<String, Object>> analyzeAllPetsAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				List<Owner> owners = ownerRepository.findAll();

				List<CompletableFuture<PetAnalysis>> futures = new ArrayList<>();

				for (Owner owner : owners) {
					for (Pet pet : owner.getPets()) {
						CompletableFuture<PetAnalysis> future = CompletableFuture
							.supplyAsync(() -> analyzeSinglePet(pet), executorService);
						futures.add(future);
					}
				}

				List<PetAnalysis> analyses = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

				return aggregateAnalysis(analyses);

			}
			catch (Exception e) {
				throw new RuntimeException("Error analyzing pets", e);
			}
		}, executorService);
	}

	private PetAnalysis analyzeSinglePet(Pet pet) {
		int visitCount = pet.getVisits() != null ? pet.getVisits().size() : 0;
		String healthStatus = determineHealthStatus(visitCount);
		int ageInYears = calculateAge(pet.getBirthDate());

		return new PetAnalysis(pet.getName(), pet.getType().getName(), ageInYears, visitCount, healthStatus);
	}

	private String determineHealthStatus(int visitCount) {
		return switch (visitCount) {
			case 0 -> "Unknown";
			case 1, 2 -> "Good";
			case 3, 4, 5 -> "Moderate";
			default -> "High Maintenance";
		};
	}

	private int calculateAge(LocalDate birthDate) {
		if (birthDate == null) {
			return 0;
		}
		return LocalDate.now().getYear() - birthDate.getYear();
	}

	private Map<String, Object> aggregateAnalysis(List<PetAnalysis> analyses) {
		Map<String, Object> result = new HashMap<>();

		Map<String, Long> typeCount = analyses.stream()
			.collect(Collectors.groupingBy(PetAnalysis::type, Collectors.counting()));

		Map<String, Long> healthStatusCount = analyses.stream()
			.collect(Collectors.groupingBy(PetAnalysis::healthStatus, Collectors.counting()));

		double averageAge = analyses.stream().mapToInt(PetAnalysis::ageInYears).average().orElse(0.0);

		int totalVisits = analyses.stream().mapToInt(PetAnalysis::visitCount).sum();

		result.put("totalPets", analyses.size());
		result.put("petsByType", typeCount);
		result.put("petsByHealthStatus", healthStatusCount);
		result.put("averageAge", averageAge);
		result.put("totalVisits", totalVisits);
		result.put("analysisDate", LocalDate.now().toString());

		return result;
	}

	public record PetAnalysis(String name, String type, int ageInYears, int visitCount, String healthStatus) {
	}

}
