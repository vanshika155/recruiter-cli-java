package com.hackindia.recruitercli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    private static final Path CANDIDATES_FILE = Path.of("data/candidates.csv");
    private static final Path JOB_FILE = Path.of("data/job.csv");

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception exception) {
            System.err.println("Could not run RecruiterCLI: " + exception.getMessage());
            System.err.println("Run the command from the project root and check the data files.");
            System.exit(1);
        }
    }

    private static void run() throws IOException {
        List<String> jobLines = Files.readAllLines(JOB_FILE);
        if (jobLines.size() < 2) {
            throw new IllegalArgumentException("job.csv must contain a header and one job row.");
        }

        String[] job = jobLines.get(1).split(",", -1);
        if (job.length != 3) {
            throw new IllegalArgumentException("Invalid job row in data/job.csv");
        }
        String role = job[0].trim();
        Set<String> requiredSkills = new TreeSet<>();
        for (String skill : job[1].split(";")) {
            if (!skill.isBlank()) requiredSkills.add(skill.trim().toLowerCase());
        }
        int minimumExperience = Integer.parseInt(job[2].trim());

        List<Candidate> candidates = new ArrayList<>();
        Map<String, Integer> recruiterCounts = new HashMap<>();
        List<String> candidateLines = Files.readAllLines(CANDIDATES_FILE);
        for (int i = 1; i < candidateLines.size(); i++) {
            if (candidateLines.get(i).isBlank()) continue;
            Candidate candidate = Candidate.fromCsv(candidateLines.get(i));
            candidates.add(candidate);
            recruiterCounts.merge(candidate.recruiter(), 1, Integer::sum);
        }

        CandidateMatcher matcher = new SkillExperienceMatcher();
        List<MatchResult> results = candidates.stream()
                .map(candidate -> matcher.score(candidate, requiredSkills, minimumExperience))
                .sorted(Comparator.comparingInt(MatchResult::score).reversed()
                        .thenComparing(result -> result.candidate().name()))
                .toList();

        System.out.println("=== RecruiterCLI: Candidate Shortlisting ===");
        System.out.println("Role: " + role);
        System.out.println("Required skills: " + requiredSkills);
        System.out.println("Minimum experience: " + minimumExperience + " year(s)\n");
        System.out.printf("%-5s %-18s %-6s %-15s %s%n",
                "Rank", "Candidate", "Score", "Skills matched", "Experience");
        System.out.println("----  -----------------  -----  --------------  ----------");

        int rank = 1;
        for (MatchResult result : results) {
            Candidate candidate = result.candidate();
            System.out.printf("%-5d %-18s %-6s %-15s %d years%n",
                    rank++,
                    candidate.name(),
                    result.score() + "%",
                    result.matchedSkills() + "/" + result.totalSkills(),
                    candidate.experience());
        }

        System.out.println("\nRecruiter candidate counts:");
        recruiterCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
    }
}
