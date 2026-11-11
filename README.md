# RecruiterCLI — Candidate Shortlisting Tool

A small Java command-line application that reads candidate profiles and job requirements from sample CSV files, scores each candidate, and prints a ranked shortlist.

## Features

- Reads candidate data from `data/candidates.csv`.
- Reads the job's required skills and minimum experience from `data/job.csv`.
- Uses a `Set<String>` to store skills without duplicates.
- Uses a `Map<String, Integer>` to count candidates by recruiter.
- Uses the `CandidateMatcher` interface so matching strategies can be replaced without rewriting the CLI.
- Prints candidates ranked by match score.

## Requirements

- Java 17 or newer
- Apache Maven 3.8+

Check your installations:

```bash
java -version
mvn -version
```

## Build

From the project root:

```bash
mvn clean package
```

## Run

```bash
java -cp target/classes com.hackindia.recruitercli.Main
```

The program reads the sample files from the `data/` folder. Run the command from the project root so those relative paths resolve correctly.

## Sample data

- `data/candidates.csv` contains candidate names, email addresses, skills, years of experience, and recruiter names.
- `data/job.csv` contains the role title, required skills, and minimum experience.

Edit these files to try different candidate profiles and job requirements. Skills are separated by semicolons.

## Example output

```text
=== RecruiterCLI: Candidate Shortlisting ===
Role: Java Backend Developer
Required skills: [java, sql, git, spring]
Minimum experience: 1 year(s)

Rank  Candidate          Score  Skills matched  Experience
----  -----------------  -----  --------------  ----------
1     Asha Verma         100%   4/4             3 years
2     Rohan Mehta         75%   3/4             2 years
3     Neha Singh          50%   2/4             1 years

Recruiter candidate counts:
  Priya Sharma: 2
  Amit Rao: 2
  Karan Das: 1
```

Scores combine skill coverage (70%) and experience (30%). Experience earns the full experience component when the candidate meets the minimum; otherwise it is proportional. This is a demonstration project, not an automated hiring decision system.

## Project structure

```text
RecruiterCLI/
├── data/
│   ├── candidates.csv
│   └── job.csv
├── src/main/java/com/hackindia/recruitercli/
│   ├── Candidate.java
│   ├── CandidateMatcher.java
│   ├── MatchResult.java
│   ├── SkillExperienceMatcher.java
│   └── Main.java
├── pom.xml
└── README.md
```

## Customizing the matching strategy

Create another class that implements `CandidateMatcher`, then pass that implementation to the shortlist logic in `Main`. The command-line interface does not need to know the scoring details.

## Notes for submission

1. Create a **public** GitHub repository named `recruiter-cli-java`.
2. Upload the contents of this folder (not the ZIP file itself).
3. Make your own commits while signed in as **@vanshika155**. Do not claim commits made by someone else as your work.
4. Confirm that `pom.xml`, `src/`, `data/`, and `README.md` are visible on the repository page.
5. In HackIndia, submit the public repository URL.

## License

MIT
