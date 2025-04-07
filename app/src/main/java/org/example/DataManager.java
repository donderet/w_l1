package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataManager {

  private final ObjectMapper objectMapper;

  public DataManager(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public DataManager() {
    this(new ObjectMapper());
  }

  public void exportDisciplines(List<Discipline> disciplines, String filename, Comparator<Discipline> comparator)
      throws IOException {
    List<Discipline> sortedDisciplines = disciplines.stream()
        .sorted(comparator)
        .collect(Collectors.toList());
    objectMapper.writeValue(new File(filename), sortedDisciplines);
  }

  public List<Discipline> importDisciplines(String filename) throws IOException {
    return objectMapper.readValue(new File(filename),
        new TypeReference<List<Discipline>>() {
        });
  }

  public void exportStudents(List<Student> students, String filename, Comparator<Student> comparator)
      throws IOException {
    List<Student> sortedStudents = students.stream()
        .sorted(comparator)
        .collect(Collectors.toList());
    objectMapper.writeValue(new File(filename), sortedStudents);
  }

  public List<Student> importStudents(String filename) throws IOException {
    return objectMapper.readValue(new File(filename),
        new TypeReference<List<Student>>() {
        });
  }

  public void exportSchool(School school, String filename, Comparator<Discipline> disciplineComparator,
      Comparator<Student> studentComparator) throws IOException {
    List<Discipline> sortedDisciplines = school.getDisciplines().stream()
        .sorted(disciplineComparator)
        .collect(Collectors.toList());
    List<Student> sortedStudents = school.getStudents().stream()
        .sorted(studentComparator)
        .collect(Collectors.toList());

    Map<String, Object> schoolMap = Map.of(
        "name", school.getName(),
        "disciplines", sortedDisciplines,
        "students", sortedStudents);
    objectMapper.writeValue(new File(filename), schoolMap);
  }

  public School importSchool(String filename) throws IOException {
    Map<String, Object> schoolData = objectMapper.readValue(new File(filename),
        new TypeReference<Map<String, Object>>() {
        });

    String name = (String) schoolData.get("name");
    List<Discipline> disciplines = objectMapper.convertValue(schoolData.get("disciplines"),
        new TypeReference<List<Discipline>>() {
        });
    List<Student> students = objectMapper.convertValue(schoolData.get("students"),
        new TypeReference<List<Student>>() {
        });

    School school = new School(name);
    if (disciplines != null) {
      for (Discipline discipline : disciplines) {
        school.addDiscipline(discipline);
      }
    }
    if (students != null) {
      for (Student student : students) {
        try {
          school.addStudent(student);
        } catch (IllegalArgumentException e) {
          System.err.println("Попередження при імпорті школи: " + e.getMessage());
        }
      }
    }
    return school;
  }
}
