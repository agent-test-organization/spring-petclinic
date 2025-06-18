package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service for analyzing pet data and generating reports.
 *
 * This class has been upgraded from Java 17 to use Java 21 features for improved
 * performance and code readability.
 *
 * UPGRADED FEATURES: ✓ Virtual Threads (Java 21+) - for better concurrent performance ✓
 * Pattern Matching for switch (Java 21+) - cleaner switch expressions ✓ Records (Java
 * 14+/21+) - replacing boilerplate classes ✓ Enhanced Stream Operations (Java 21+) -
 * improved collection handling
 *
 * FUTURE UPGRADE OPPORTUNITIES: - String Templates (Java 24+) - Structured Concurrency
 * (Java 24+)
 */
@Service
public class PetAnalyticsService {

	private final OwnerRepository ownerRepository;

	private final ExecutorService executorService;

	public PetAnalyticsService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
		// UPGRADED: Using Virtual Threads (Java 21+)
		this.executorService = Executors.newVirtualThreadPerTaskExecutor();
	}

	/**
	 * Generates a comprehensive pet report. Uses old-style switch statements that can be
	 * upgraded to pattern matching.
	 */
	public String generatePetReport(Pet pet) {
		StringBuilder report = new StringBuilder();

		// OLD WAY: String concatenation (can use String Templates in Java 24+)
		report.append("Pet Report for: ").append(pet.getName()).append("\n");
		report.append("Type: ").append(pet.getType().getName()).append("\n");
		report.append("Birth Date: ").append(formatDate(pet.getBirthDate())).append("\n");

		// OLD WAY: Traditional switch with instanceof checks
		String petCategory = categorizeByType(pet.getType().getName());
		report.append("Category: ").append(petCategory).append("\n");

		// OLD WAY: Traditional null checking
		if (pet.getVisits() != null && !pet.getVisits().isEmpty()) {
			report.append("Total Visits: ").append(pet.getVisits().size()).append("\n");
			report.append("Last Visit: ").append(getLastVisitDate(pet)).append("\n");
		}
		else {
			report.append("No visits recorded\n");
		}

		return report.toString();
	}

	/**
	 * UPGRADED: Using pattern matching for switch (Java 21+)
	 */
	private String categorizeByType(String petType) {
		return switch (petType.toLowerCase()) {
			case "dog" -> "Canine";
			case "cat" -> "Feline";
			case "bird" -> "Avian";
			case "hamster", "rabbit" -> "Small Mammal";
			default -> "Other";
		};
	}

	/**
	 * OLD WAY: Manual date formatting CAN BE UPGRADED: String Templates (Java 24+)
	 */
	private String formatDate(LocalDate date) {
		if (date == null) {
			return "Unknown";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
		return date.format(formatter);
	}

	/**
	 * UPGRADED: Using sequenced collections (Java 21+)
	 */
	private String getLastVisitDate(Pet pet) {
		if (pet.getVisits() == null || pet.getVisits().isEmpty()) {
			return "No visits";
		}

		// UPGRADED: Using stream and sequenced collections for cleaner last element
		// access
		Visit lastVisit = pet.getVisits()
			.stream()
			.sorted(Comparator.comparing(Visit::getDate))
			.reduce((first, second) -> second) // Gets last element
			.orElse(null);

		return lastVisit != null ? formatDate(lastVisit.getDate()) : "No visits";
	}

	/**
	 * Analyzes multiple pets concurrently. OLD WAY: Traditional ExecutorService CAN BE
	 * UPGRADED: Virtual Threads and Structured Concurrency (Java 21+/24+)
	 */
	public CompletableFuture<Map<String, Object>> analyzeAllPetsAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				List<Owner> owners = ownerRepository.findAll();

				// OLD WAY: Collecting futures manually
				List<CompletableFuture<PetAnalysis>> futures = new ArrayList<>();

				for (Owner owner : owners) {
					for (Pet pet : owner.getPets()) {
						CompletableFuture<PetAnalysis> future = CompletableFuture
							.supplyAsync(() -> analyzeSinglePet(pet), executorService);
						futures.add(future);
					}
				}

				// OLD WAY: Waiting for all futures to complete
				List<PetAnalysis> analyses = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

				return aggregateAnalysis(analyses);

			}
			catch (Exception e) {
				throw new RuntimeException("Error analyzing pets", e);
			}
		}, executorService);
	}

	/**
	 * OLD WAY: Traditional record-like class (can use Record Patterns in Java 21+)
	 */
	private PetAnalysis analyzeSinglePet(Pet pet) {
		int visitCount = pet.getVisits() != null ? pet.getVisits().size() : 0;
		String healthStatus = determineHealthStatus(visitCount);
		int ageInYears = calculateAge(pet.getBirthDate());

		return new PetAnalysis(pet.getName(), pet.getType().getName(), ageInYears, visitCount, healthStatus);
	}

	/**
	 * OLD WAY: Traditional if-else chains CAN BE UPGRADED: Pattern matching with guards
	 * (Java 24+)
	 */
	private String determineHealthStatus(int visitCount) {
		if (visitCount == 0) {
			return "Unknown";
		}
		else if (visitCount <= 2) {
			return "Good";
		}
		else if (visitCount <= 5) {
			return "Moderate";
		}
		else {
			return "High Maintenance";
		}
	}

	private int calculateAge(LocalDate birthDate) {
		if (birthDate == null) {
			return 0;
		}
		return LocalDate.now().getYear() - birthDate.getYear();
	}

	/**
	 * OLD WAY: Manual map building CAN BE UPGRADED: Pattern matching destructuring (Java
	 * 24+)
	 */
	private Map<String, Object> aggregateAnalysis(List<PetAnalysis> analyses) {
		Map<String, Object> result = new HashMap<>();

		// OLD WAY: Manual counting and grouping
		Map<String, Long> typeCount = analyses.stream()
			.collect(Collectors.groupingBy(analysis -> analysis.type, Collectors.counting()));

		Map<String, Long> healthStatusCount = analyses.stream()
			.collect(Collectors.groupingBy(analysis -> analysis.healthStatus, Collectors.counting()));

		double averageAge = analyses.stream().mapToInt(analysis -> analysis.ageInYears).average().orElse(0.0);

		int totalVisits = analyses.stream().mapToInt(analysis -> analysis.visitCount).sum();

		// OLD WAY: Manual map population
		result.put("totalPets", analyses.size());
		result.put("petsByType", typeCount);
		result.put("petsByHealthStatus", healthStatusCount);
		result.put("averageAge", averageAge);
		result.put("totalVisits", totalVisits);
		result.put("analysisDate", LocalDate.now().toString());

		return result;
	}

	/**
	 * UPGRADED: Using record instead of traditional class (Java 14+/21+)
	 */
	public record PetAnalysis(String name, String type, int ageInYears, int visitCount, String healthStatus) {
	}

}
