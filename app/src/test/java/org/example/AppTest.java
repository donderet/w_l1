package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

class AppTest {

  @Test
  void testDisciplineEquality() {
    Discipline math1 = new Discipline("Математика");
    Discipline math2 = new Discipline("Математика");
    Discipline physics = new Discipline("Фізика");
    assertEquals(math1, math2);
    assertEquals(math1.hashCode(), math2.hashCode());
    assertNotEquals(math1, physics);
  }

  @Test
  void testInvalidDisciplineCreation() {
    assertThrows(IllegalArgumentException.class, () -> new Discipline(null));
    assertThrows(IllegalArgumentException.class, () -> new Discipline(""));
    assertThrows(IllegalArgumentException.class, () -> new Discipline(" "));
  }

  @Test
  void testAddValidGradeToStudent() {
    Student student = new Student("Іван", "Петренко");
    Discipline discipline = new Discipline("Українська мова");
    student.addGrade(discipline, 10);
    assertEquals(1, student.getGrades().get(discipline).size());
    assertEquals(10, student.getGrades().get(discipline).get(0));
  }

  @Test
  void testCalculateStudentAverageGrade() {
    Student student = new Student("Ольга", "Сидоренко");
    Discipline physics = new Discipline("Фізика");
    student.addGrade(physics, 9);
    student.addGrade(physics, 11);
    Discipline history = new Discipline("Історія");
    student.addGrade(history, 12);
    assertEquals((9.0 + 11.0 + 12.0) / 3.0, student.calculateAverageGrade(), 0.001);
  }

  @Test
  void testAddInvalidGradeToStudent() {
    Student student = new Student("Андрій", "Ковальчук");
    Discipline chemistry = new Discipline("Хімія");
    assertThrows(IllegalArgumentException.class, () -> student.addGrade(chemistry, 0));
    assertThrows(IllegalArgumentException.class, () -> student.addGrade(chemistry, 13));
  }

  @Test
  void testAddAndFindDisciplineInSchool() {
    School school = new School("Перша Школа");
    Discipline informatics = new Discipline("Інформатика");
    school.addDiscipline(informatics);
    Optional<Discipline> foundDiscipline = school.findDisciplineByName("Інформатика");
    assertTrue(foundDiscipline.isPresent());
    assertEquals("Інформатика", foundDiscipline.get().getName());
  }

  @Test
  void testAddAndRemoveStudentFromSchool() {
    School school = new School("Друга Школа");
    Student student = new Student("Марія", "Лисенко");
    school.addStudent(student);
    school.removeStudent(student.getId());
    Optional<Student> foundStudent = school.findStudentById(student.getId());
    assertTrue(foundStudent.isEmpty());
  }

  @Test
  void testCalculateSchoolAverageGrade() {
    School school = new School("Гімназія №1");
    Student student1 = new Student("Богдан", "Шевченко");
    Discipline math = new Discipline("Математика");
    student1.addGrade(math, 9);
    student1.addGrade(math, 10);
    school.addStudent(student1);

    Student student2 = new Student("Софія", "Мельник");
    Discipline physics = new Discipline("Фізика");
    student2.addGrade(physics, 8);
    school.addStudent(student2);

    assertEquals((9.0 + 10.0 + 8.0) / 3.0, school.calculateSchoolAverageGrade(), 0.001);
  }

  @Test
  void testRemoveDisciplineByName() {
    School school = new School("Ліцей 'Інтелект'");
    Discipline history = new Discipline("Історія України");
    school.addDiscipline(history);
    assertTrue(school.findDisciplineByName("Історія України").isPresent());
    school.removeDisciplineByName("Історія України");
    assertFalse(school.findDisciplineByName("Історія України").isPresent());
  }
}
