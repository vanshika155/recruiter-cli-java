package com.hackindia.recruitercli;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public record Candidate(
        String name,
        String email,
        Set<String> skills,
        int experience,
        String recruiter
) {
    public Candidate {
        skills = skills.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(skill -> !skill.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Candidate fromCsv(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid candidate row: " + line);
        }
        Set<String> skills = Arrays.stream(parts[2].split(";"))
                .map(String::trim)
                .collect(Collectors.toCollection(TreeSet::new));
        return new Candidate(
                parts[0].trim(),
                parts[1].trim(),
                skills,
                Integer.parseInt(parts[3].trim()),
                parts[4].trim()
        );
    }
}
