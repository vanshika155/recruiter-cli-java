package com.hackindia.recruitercli;

import java.util.Set;

public class SkillExperienceMatcher implements CandidateMatcher {
    @Override
    public MatchResult score(Candidate candidate, Set<String> requiredSkills, int minimumExperience) {
        long matched = requiredSkills.stream()
                .filter(candidate.skills()::contains)
                .count();

        double skillScore = requiredSkills.isEmpty()
                ? 100.0
                : (matched * 100.0 / requiredSkills.size());

        double experienceScore;
        if (minimumExperience <= 0) {
            experienceScore = 100.0;
        } else {
            experienceScore = Math.min(100.0,
                    candidate.experience() * 100.0 / minimumExperience);
        }

        int totalScore = (int) Math.round(skillScore * 0.70 + experienceScore * 0.30);
        return new MatchResult(candidate, totalScore, (int) matched, requiredSkills.size());
    }
}
