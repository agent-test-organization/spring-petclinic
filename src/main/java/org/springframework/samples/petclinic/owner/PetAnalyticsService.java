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
 * This class intentionally uses Java 17 patterns that can be upgraded
 * to leverage Java 24 features for migration testing purposes.
 *
 * UPGRADE OPPORTUNITIES:
 * - Virtual Threads (Java 21+)
 * - Pattern Matching for switch (Java 21+)
 * - Record Patterns (Java 21+)
 * - Sequenced Collections (Java 21+)
 * - String Templates (Java 24+)
 * - Structured Concurrency (Java 24+)
 */
@Service
public class PetAnalyticsService {

	private final OwnerRepository ownerRepository;
	private final ExecutorService executorService;

	public PetAnalyticsService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
		// OLD WAY: Traditional thread pool (can be upgraded to Virtual Threads)
		this.executorService = Executors.newFixedThreadPool(10);
	}

	/**
	 * Generates a comprehensive pet report.
	 * Uses old-style switch statements that can be upgraded to pattern matching.
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
		} else {
			report.append("No visits recorded\n");
		}

		return report.toString();
	}

	/**
	 * OLD WAY: Traditional switch statement
	 * CAN BE UPGRADED: Pattern matching for switch (Java 21+)
	 */
	private String categorizeByType(String petType) {
		switch (petType.toLowerCase()) {
			case "dog":
				return "Canine";
			case "cat":
				return "Feline";
			case "bird":
				return "Avian";
			case "hamster":
			case "rabbit":
				return "Small Mammal";
			default:
				return "Other";
		}
	}

	/**
	 * OLD WAY: Manual date formatting
	 * CAN BE UPGRADED: String Templates (Java 24+)
	 */
	private String formatDate(LocalDate date) {
		if (date == null) {
			return "Unknown";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
		return date.format(formatter);
	}

	/**
	 * OLD WAY: Manual collection processing
	 * CAN BE UPGRADED: Sequenced Collections (Java 21+)
	 */
	private String getLastVisitDate(Pet pet) {
		if (pet.getVisits() == null || pet.getVisits().isEmpty()) {
			return "No visits";
		}

		// OLD WAY: Converting to list and getting last element
		List<Visit> visitList = new ArrayList<>(pet.getVisits());
		visitList.sort(Comparator.comparing(Visit::getDate));
		Visit lastVisit = visitList.get(visitList.size() - 1);

		return formatDate(lastVisit.getDate());
	}

	/**
	 * Analyzes multiple pets concurrently.
	 * OLD WAY: Traditional ExecutorService
	 * CAN BE UPGRADED: Virtual Threads and Structured Concurrency (Java 21+/24+)
	 */
	public CompletableFuture<Map<String, Object>> analyzeAllPetsAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				List<Owner> owners = ownerRepository.findAll();

				// OLD WAY: Collecting futures manually
				List<CompletableFuture<PetAnalysis>> futures = new ArrayList<>();

				for (Owner owner : owners) {
					for (Pet pet : owner.getPets()) {
						CompletableFuture<PetAnalysis> future = CompletableFuture.supplyAsync(
							() -> analyzeSinglePet(pet), executorService);
						futures.add(future);
					}
				}

				// OLD WAY: Waiting for all futures to complete
				List<PetAnalysis> analyses = futures.stream()
					.map(CompletableFuture::join)
					.collect(Collectors.toList());

				return aggregateAnalysis(analyses);

			} catch (Exception e) {
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

		return new PetAnalysis(
			pet.getName(),
			pet.getType().getName(),
			ageInYears,
			visitCount,
			healthStatus
		);
	}

	/**
	 * OLD WAY: Traditional if-else chains
	 * CAN BE UPGRADED: Pattern matching with guards (Java 24+)
	 */
	private String determineHealthStatus(int visitCount) {
		if (visitCount == 0) {
			return "Unknown";
		} else if (visitCount <= 2) {
			return "Good";
		} else if (visitCount <= 5) {
			return "Moderate";
		} else {
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
	 * OLD WAY: Manual map building
	 * CAN BE UPGRADED: Pattern matching destructuring (Java 24+)
	 */
	private Map<String, Object> aggregateAnalysis(List<PetAnalysis> analyses) {
		Map<String, Object> result = new HashMap<>();

		// OLD WAY: Manual counting and grouping
		Map<String, Long> typeCount = analyses.stream()
			.collect(Collectors.groupingBy(
				analysis -> analysis.type,
				Collectors.counting()
			));

		Map<String, Long> healthStatusCount = analyses.stream()
			.collect(Collectors.groupingBy(
				analysis -> analysis.healthStatus,
				Collectors.counting()
			));

		double averageAge = analyses.stream()
			.mapToInt(analysis -> analysis.ageInYears)
			.average()
			.orElse(0.0);

		int totalVisits = analyses.stream()
			.mapToInt(analysis -> analysis.visitCount)
			.sum();

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
	 * OLD WAY: Traditional class instead of record
	 * CAN BE UPGRADED: Use records with pattern matching (Java 21+)
	 */
	public static class PetAnalysis {
		public final String name;
		public final String type;
		public final int ageInYears;
		public final int visitCount;
		public final String healthStatus;

		public PetAnalysis(String name, String type, int ageInYears, int visitCount, String healthStatus) {
			this.name = name;
			this.type = type;
			this.ageInYears = ageInYears;
			this.visitCount = visitCount;
			this.healthStatus = healthStatus;
		}

		// OLD WAY: Manual equals, hashCode, toString
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			PetAnalysis that = (PetAnalysis) obj;
			return ageInYears == that.ageInYears &&
				visitCount == that.visitCount &&
				Objects.equals(name, that.name) &&
				Objects.equals(type, that.type) &&
				Objects.equals(healthStatus, that.healthStatus);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, type, ageInYears, visitCount, healthStatus);
		}

		@Override
		public String toString() {
			return "PetAnalysis{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", ageInYears=" + ageInYears +
				", visitCount=" + visitCount +
				", healthStatus='" + healthStatus + '\'' +
				'}';
		}
	}
}
