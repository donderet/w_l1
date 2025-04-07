package org.example;

import java.util.*;

public class School {
  private String name;
  private Map<Long, Student> students;
  private Set<Discipline> disciplines;

  public School(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Назва школи не може бути порожньою.");
    }
    this.name = name;
    this.students = new HashMap<>();
    this.disciplines = new HashSet<>();
  }

  public String getName() {
    return name;
  }

  public boolean addDiscipline(Discipline discipline) {
    if (discipline == null) {
      throw new IllegalArgumentException("Не можна додати null як дисципліну.");
    }
    boolean added = disciplines.add(discipline);
    if (!added) {
      System.out.println("Примітка: Дисципліна '" + discipline.getName() +
          "' вже існує в школі.");
    }
    return added;
  }

  public Optional<Discipline> findDisciplineByName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return Optional.empty();
    }
    return disciplines.stream()
        .filter(d -> d.getName().equalsIgnoreCase(name))
        .findFirst();
  }

  public boolean removeDiscipline(Discipline discipline) {
    if (discipline == null) {
      throw new IllegalArgumentException("Не можна видалити null як дисципліну.");
    }
    boolean removed = disciplines.remove(discipline);
    if (!removed) {
      throw new NoSuchElementException("Дисципліну '" + discipline.getName() +
          "' не знайдено в школі для видалення.");
    }
    System.out.println("Дисципліну '" + discipline.getName() + "' видалено зі школи.");
    return true;
  }

  public boolean removeDisciplineByName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Назва дисципліни для видалення не може бути порожньою.");
    }
    Optional<Discipline> disciplineOpt = findDisciplineByName(name);
    if (disciplineOpt.isPresent()) {
      return removeDiscipline(disciplineOpt.get());
    } else {
      throw new NoSuchElementException("Дисципліну '" + name +
          "' не знайдено в школі для видалення.");
    }
  }

  public Set<Discipline> getDisciplines() {
    return new HashSet<>(disciplines);
  }

  public void addStudent(Student student) {
    if (student == null) {
      throw new IllegalArgumentException("Не можна додати null як учня.");
    }
    if (students.containsKey(student.getId())) {
      throw new IllegalArgumentException(
          "Учень з ID " + student.getId() + " (" + student.getFullName() + ") вже існує.");
    }
    students.put(student.getId(), student);
    System.out
        .println("Учень " + student.getFullName() +
            " (ID: " + student.getId() + ") доданий до школи '" + name + "'.");
  }

  public Optional<Student> findStudentById(long id) {
    return Optional.ofNullable(students.get(id));
  }

  public void updateStudent(Student updatedStudent) {
    if (updatedStudent == null) {
      throw new IllegalArgumentException("Не можна оновити дані учня на null.");
    }
    long id = updatedStudent.getId();
    if (findStudentById(id).isPresent()) {
      students.put(id, updatedStudent);
      System.out.println("Дані учня " + updatedStudent.getFullName()
          + " (ID: " + id + ") оновлено.");
    } else {
      throw new NoSuchElementException("Учня з ID " + id + " не знайдено для оновлення.");
    }
  }

  public Student removeStudent(long id) {
    Student studentToRemove = findStudentById(id)
        .orElseThrow(() -> new NoSuchElementException(
            "Учня з ID " + id + " не знайдено для видалення."));

    students.remove(id);
    System.out
        .println("Учень " + studentToRemove.getFullName() +
            " (ID: " + id + ") видалений зі школи '" + name + "'.");
    return studentToRemove;
  }

  public List<Student> getStudents() {
    return new ArrayList<>(students.values());
  }

  public double calculateSchoolAverageGrade() {
    return students.values().stream()
        .flatMap(student -> student.getGrades().values().stream())
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .average()
        .orElse(0.0);
  }

  public void displayStudentInfo(long studentId) {
    findStudentById(studentId).ifPresentOrElse(
        System.out::println,
        () -> System.out.println("Учня з ID " + studentId + " не знайдено."));
  }

  public void displayStudentsInfo() {
    System.out.println("\n--- Інформація про учнів школи '" + name + "' ---");
    if (students.isEmpty()) {
      System.out.println("У школі ще немає учнів.");
      return;
    }
    students.values().forEach(student -> {
      System.out.println(student);
      System.out.println("--------------------");
    });
  }

  public void displayDisciplines() {
    System.out.println("\n--- Дисципліни у школі '" + name + "' ---");
    if (disciplines.isEmpty()) {
      System.out.println("У школі ще немає зареєстрованих дисциплін.");
      return;
    }
    disciplines.forEach(System.out::println);
  }
}
