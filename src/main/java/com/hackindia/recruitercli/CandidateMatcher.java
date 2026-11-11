package com.hackindia.recruitercli;

import java.util.Set;

/**
 * Strategy interface for candidate scoring.
 * New matching rules can be added without changing the CLI.
 */
public interface CandidateMatcher {
    MatchResult score(Candidate candidate, Set<String> requiredSkills, int minimumExperience);
}
