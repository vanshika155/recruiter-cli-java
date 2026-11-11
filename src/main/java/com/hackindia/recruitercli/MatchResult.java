package com.hackindia.recruitercli;

public record MatchResult(Candidate candidate, int score, int matchedSkills, int totalSkills) {
}
