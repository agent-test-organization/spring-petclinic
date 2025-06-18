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
        } else {
            report.append("No visits recorded\n");
        }

        return report.toString();
    }

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

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "Unknown";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }

    private String getLastVisitDate(Pet pet) {
        if (pet.getVisits() == null || pet.getVisits().isEmpty()) {
            return "No visits";
        }

        List<Visit> visitList = new ArrayList<>(pet.getVisits());
        visitList.sort(Comparator.comparing(Visit::getDate));
        Visit lastVisit = visitList.get(visitList.size() - 1);

        return formatDate(lastVisit.getDate());
    }

    public CompletableFuture<Map<String, Object>> analyzeAllPetsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Owner> owners = ownerRepository.findAll();

                List<CompletableFuture<PetAnalysis>> futures = new ArrayList<>();

                for (Owner owner : owners) {
                    for (Pet pet : owner.getPets()) {
                        CompletableFuture<PetAnalysis> future = CompletableFuture.supplyAsync(
                            () -> analyzeSinglePet(pet), executorService);
                        futures.add(future);
                    }
                }

                List<PetAnalysis> analyses = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

                return aggregateAnalysis(analyses);

            } catch (Exception e) {
                throw new RuntimeException("Error analyzing pets", e);
            }
        }, executorService);
    }

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

    private Map<String, Object> aggregateAnalysis(List<PetAnalysis> analyses) {
        Map<String, Object> result = new HashMap<>();

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

        result.put("totalPets", analyses.size());
        result.put("petsByType", typeCount);
        result.put("petsByHealthStatus", healthStatusCount);
        result.put("averageAge", averageAge);
        result.put("totalVisits", totalVisits);
        result.put("analysisDate", LocalDate.now().toString());

        return result;
    }

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
