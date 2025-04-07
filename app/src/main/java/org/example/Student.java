package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.util.stream.Collectors;

public class Student {
  private static long nextId = 1;

  private final long id;
  private String firstName;
  private String lastName;
  private Map<Discipline, List<Integer>> grades;

  @JsonCreator
  public Student(@JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName) {
    if (firstName == null ||
        firstName.trim().isEmpty() ||
        lastName == null ||
        lastName.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Ім'я та прізвище учня не можуть бути порожніми.");
    }
    this.id = nextId++;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grades = new HashMap<>();
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return lastName + " " + firstName;
  }

  public Map<Discipline, List<Integer>> getGrades() {
    return new HashMap<>(grades);
  }

  public Set<Discipline> getDisciplines() {
    return new HashSet<>(grades.keySet());
  }

  public void setFirstName(String firstName) {
    if (firstName == null || firstName.trim().isEmpty()) {
      throw new IllegalArgumentException("Ім'я учня не може бути порожнім.");
    }
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    if (lastName == null || lastName.trim().isEmpty()) {
      throw new IllegalArgumentException("Прізвище учня не може бути порожнім.");
    }
    this.lastName = lastName;
  }

  public void addGrade(Discipline discipline, int grade) {
    if (discipline == null) {
      throw new IllegalArgumentException("Дисципліна не може бути null.");
    }
    if (grade < 1 || grade > 12) {
      throw new IllegalArgumentException(
          "Невалідна оцінка: " + grade +
              " для " + getFullName() + " (має бути від 1 до 12).");
    }
    List<Integer> disciplineGrades = grades.computeIfAbsent(discipline, k -> new ArrayList<>());
    disciplineGrades.add(grade);
  }

  public double calculateAverageGrade() {
    return grades.values().stream()
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .average()
        .orElse(0.0);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb
        .append("Учень ID: ").append(id).append("\n")
        .append("  Ім'я: ").append(getFullName()).append("\n");
    sb.append("  Дисципліни та Оцінки:\n");
    if (grades.isEmpty()) {
      sb.append("    Оцінок ще немає.\n");
    } else {
      grades.forEach((discipline, gradeList) -> {
        sb
            .append("    - ").append(discipline.getName()).append(": ")
            .append(gradeList.stream().map(String::valueOf).collect(Collectors.joining(", ")))
            .append("\n");
      });
    }
    double overallAvg = calculateAverageGrade();
    sb.append(String.format(Locale.US, "  Загальний середній бал: %.2f\n", overallAvg));
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Student student = (Student) o;
    return id == student.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
