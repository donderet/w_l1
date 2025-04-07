package org.example;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class App {

  private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
  private static School school = new School("Моя Чудова Школа");
  private static final DataManager dataManager = new DataManager();
  private static final String DISCIPLINES_FILE = "disciplines.json";
  private static final String STUDENTS_FILE = "students.json";
  private static final String SCHOOL_FILE = "school.json";

  public static void main(String[] args) {
    boolean running = true;

    while (running) {
      displayMenu();
      int choice = getUserChoice();

      try {
        switch (choice) {
          case 1:
            handleAddDiscipline();
            break;
          case 2:
            handleListDisciplines();
            break;
          case 3:
            handleRemoveDiscipline();
            break;
          case 4:
            handleAddStudent();
            break;
          case 5:
            handleListStudents();
            break;
          case 6:
            handleFindStudent();
            break;
          case 7:
            handleUpdateStudentName();
            break;
          case 8:
            handleRemoveStudent();
            break;
          case 9:
            handleAddGrade();
            break;
          case 10:
            handleSchoolAverage();
            break;

          case 11:
            handleExportDisciplines();
            break;
          case 12:
            handleImportDisciplines();
            break;
          case 13:
            handleExportStudents();
            break;
          case 14:
            handleImportStudents();
            break;
          case 15:
            handleExportSchool();
            break;
          case 16:
            handleImportSchool();
            break;

          case 0:
            running = false;
            System.out.println("Завершення роботи програми.");
            break;
          default:
            System.out.println("Невірний вибір. Спробуйте ще раз.");
        }
      } catch (IllegalArgumentException | NoSuchElementException e) {
        System.err.println("Помилка операції: " + e.getMessage());
      } catch (IOException e) {
        System.err.println("Помилка файлової операції: " + e.getMessage());
      } catch (Exception e) {
        System.err.println("Сталася неочікувана помилка: " + e.getMessage());
        e.printStackTrace();
      }

      if (running) {
        System.out.println("\nНатисніть Enter для продовження...");
        if (choice != -1) {
        } else {
          if (scanner.hasNextLine())
            scanner.nextLine();
        }
        scanner.nextLine();
      }
    }

    scanner.close();
  }

  private static void displayMenu() {
    System.out.println("\n".repeat(16));
    System.out.println("--- Меню Керування Школою ---");
    System.out.println("1. Додати Дисципліну");
    System.out.println("2. Показати Список Дисциплін");
    System.out.println("3. Видалити Дисципліну");
    System.out.println("---");
    System.out.println("4. Додати Учня");
    System.out.println("5. Показати Список Учнів");
    System.out.println("6. Знайти Учня за ID");
    System.out.println("7. Оновити Ім'я Учня");
    System.out.println("8. Видалити Учня");
    System.out.println("---");
    System.out.println("9. Додати Оцінку Учню");
    System.out.println("10. Показати Середній Бал Школи");
    System.out.println("--- Імпорт/Експорт ---");
    System.out.println("11. Експорт Дисциплін у " + DISCIPLINES_FILE);
    System.out.println("12. Імпорт Дисциплін з " + DISCIPLINES_FILE);
    System.out.println("13. Експорт Учнів у " + STUDENTS_FILE);
    System.out.println("14. Імпорт Учнів з " + STUDENTS_FILE);
    System.out.println("15. Експорт Школи у " + SCHOOL_FILE);
    System.out.println("16. Імпорт Школи з " + SCHOOL_FILE);
    System.out.println("---");
    System.out.println("0. Вихід");
  }

  private static int getUserChoice() {
    System.out.print("Ваш вибір: ");
    int choice = -1;
    try {
      String line = scanner.nextLine();
      if (line.trim().isEmpty()) {
        System.err.println("Помилка: Введення не може бути порожнім.");
        return -1;
      }
      choice = Integer.parseInt(line.trim());
    } catch (NumberFormatException e) {
      System.err.println("Помилка: Очікувалось ціле число.");
    } catch (NoSuchElementException e) {
      System.err.println("Помилка: Не вдалося прочитати введення (потік закрито?).");
      choice = 0;
    }
    return choice;
  }

  private static long getLongInput(String prompt) {
    long value = -1;
    while (value < 0) {
      System.out.print(prompt);
      try {
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) {
          System.err.println("Помилка: Введення ID не може бути порожнім.");
          continue;
        }
        value = Long.parseLong(line.trim());
        if (value < 0) {
          System.err.println("Помилка: ID не може бути від'ємним.");
          value = -1;
        }
      } catch (NumberFormatException e) {
        System.err.println("Помилка: Очікувалось ціле число (ID).");
        value = -1;
      } catch (NoSuchElementException e) {
        System.err.println("Помилка: Не вдалося прочитати введення (потік закрито?).");
        throw e;
      }
    }
    return value;
  }

  private static int getIntInput(String prompt, int min, int max) {
    int value = min - 1;
    while (value < min || value > max) {
      System.out.print(prompt + " (" + min + "-" + max + "): ");
      try {
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) {
          System.err.println("Помилка: Введення не може бути порожнім.");
          continue;
        }
        value = Integer.parseInt(line.trim());
        if (value < min || value > max) {
          System.err.println("Помилка: Значення має бути в діапазоні від " + min + " до " + max + ".");
        }
      } catch (NumberFormatException e) {
        System.err.println("Помилка: Очікувалось ціле число.");
        value = min - 1;
      } catch (NoSuchElementException e) {
        System.err.println("Помилка: Не вдалося прочитати введення (потік закрито?).");
        throw e;
      }
    }
    return value;
  }

  private static void handleAddDiscipline() {
    System.out.print("Введіть назву нової дисципліни: ");
    String name = scanner.nextLine();
    if (name.trim().isEmpty()) {
      System.err.println("Помилка: Назва дисципліни не може бути порожньою.");
      return;
    }
    Discipline newDiscipline = new Discipline(name);
    if (school.addDiscipline(newDiscipline)) {
      System.out.println("Дисципліну '" + name + "' успішно додано.");
    }
  }

  private static void handleListDisciplines() {
    school.displayDisciplines();
  }

  private static void handleRemoveDiscipline() {
    System.out.print("Введіть назву дисципліни для видалення: ");
    String name = scanner.nextLine();
    school.removeDisciplineByName(name);
  }

  private static void handleAddStudent() {
    System.out.print("Введіть ім'я учня: ");
    String firstName = scanner.nextLine();
    System.out.print("Введіть прізвище учня: ");
    String lastName = scanner.nextLine();

    if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
      System.err.println("Помилка: Ім'я та прізвище не можуть бути порожніми.");
      return;
    }
    Student newStudent = new Student(firstName, lastName);
    school.addStudent(newStudent);
  }

  private static void handleListStudents() {
    school.displayStudentsInfo();
  }

  private static void handleFindStudent() {
    long studentId = getLongInput("Введіть ID учня для пошуку: ");
    school.displayStudentInfo(studentId);
  }

  private static void handleUpdateStudentName() {
    long studentId = getLongInput("Введіть ID учня для оновлення: ");
    Optional<Student> studentOpt = school.findStudentById(studentId);

    if (studentOpt.isEmpty()) {
      System.out.println("Учня з ID " + studentId + " не знайдено.");
      return;
    }

    Student studentToUpdate = studentOpt.get();
    System.out.println("Поточні дані: " + studentToUpdate.getFullName());
    System.out.print("Введіть нове ім'я (або Enter, щоб залишити '" + studentToUpdate.getFirstName() + "'): ");
    String newFirstName = scanner.nextLine();
    System.out.print("Введіть нове прізвище (або Enter, щоб залишити '" + studentToUpdate.getLastName() + "'): ");
    String newLastName = scanner.nextLine();

    boolean updated = false;
    if (!newFirstName.trim().isEmpty()) {
      studentToUpdate.setFirstName(newFirstName);
      updated = true;
    }
    if (!newLastName.trim().isEmpty()) {
      studentToUpdate.setLastName(newLastName);
      updated = true;
    }

    if (updated) {
      school.updateStudent(studentToUpdate);
    } else {
      System.out.println("Дані не змінено.");
    }
  }

  private static void handleRemoveStudent() {
    long studentId = getLongInput("Введіть ID учня для видалення: ");
    school.removeStudent(studentId);
  }

  private static void handleAddGrade() {
    long studentId = getLongInput("Введіть ID учня, якому додати оцінку: ");
    Optional<Student> studentOpt = school.findStudentById(studentId);
    if (studentOpt.isEmpty()) {
      System.out.println("Учня з ID " + studentId + " не знайдено.");
      return;
    }
    Student student = studentOpt.get();

    System.out.print("Введіть назву дисципліни: ");
    String disciplineName = scanner.nextLine();
    Optional<Discipline> disciplineOpt = school.findDisciplineByName(disciplineName);
    if (disciplineOpt.isEmpty()) {
      System.out.println("Дисципліну '" + disciplineName + "' не знайдено у школі. Спочатку додайте дисципліну.");
      return;
    }
    Discipline discipline = disciplineOpt.get();

    int grade = getIntInput("Введіть оцінку", 1, 12);

    student.addGrade(discipline, grade);
    System.out
        .println("Оцінку " + grade + " з дисципліни '" + disciplineName + "' додано учню " + student.getFullName());
  }

  private static void handleSchoolAverage() {
    double average = school.calculateSchoolAverageGrade();
    System.out.printf(Locale.US, "Середній бал по школі '%s': %.2f\n", school.getName(), average);
  }

  private static void handleExportDisciplines() throws IOException {
    List<Discipline> disciplines = List.copyOf(school.getDisciplines());
    if (disciplines.isEmpty()) {
      System.out.println("У школі немає дисциплін для експорту.");
      return;
    }
    Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);
    dataManager.exportDisciplines(disciplines, DISCIPLINES_FILE, comparator);
    System.out.println("Список дисциплін експортовано у файл " + DISCIPLINES_FILE);
  }

  private static void handleImportDisciplines() throws IOException {
    try {
      List<Discipline> importedDisciplines = dataManager.importDisciplines(DISCIPLINES_FILE);
      int count = 0;
      for (Discipline discipline : importedDisciplines) {
        if (school.addDiscipline(discipline)) {
          count++;
        }
      }
      System.out.println("Імпортовано " + importedDisciplines.size() + " дисциплін. Додано нових: " + count + ".");
    } catch (IOException e) {
      System.err.println("Помилка імпорту дисциплін з файлу " + DISCIPLINES_FILE + ": " + e.getMessage());
      System.err.println("Перевірте, чи існує файл і чи має він правильний формат.");
    }
  }

  private static void handleExportStudents() throws IOException {
    List<Student> students = school.getStudents();
    if (students.isEmpty()) {
      System.out.println("У школі немає студентів для експорту.");
      return;
    }
    Comparator<Student> comparator = Comparator.comparing(Student::getId);
    dataManager.exportStudents(students, STUDENTS_FILE, comparator);
    System.out.println("Список студентів експортовано у файл " + STUDENTS_FILE);
  }

  private static void handleImportStudents() throws IOException {
    try {
      List<Student> importedStudents = dataManager.importStudents(STUDENTS_FILE);
      int count = 0;
      int skipped = 0;
      for (Student student : importedStudents) {
        try {
          school.addStudent(student);
          count++;
        } catch (IllegalArgumentException e) {
          System.err.println("Попередження: " + e.getMessage());
          skipped++;
        }
      }
      System.out.println("Імпортовано " + importedStudents.size() + " студентів. Додано нових: " + count
          + ". Пропущено існуючих: " + skipped + ".");
      System.out.println("Увага: Оцінки студентів при такому імпорті не відновлюються!");
    } catch (IOException e) {
      System.err.println("Помилка імпорту студентів з файлу " + STUDENTS_FILE + ": " + e.getMessage());
      System.err.println("Перевірте, чи існує файл і чи має він правильний формат.");
    }
  }

  private static void handleExportSchool() throws IOException {
    Comparator<Discipline> disciplineComparator = Comparator.comparing(Discipline::getName);
    Comparator<Student> studentComparator = Comparator.comparing(Student::getId);
    dataManager.exportSchool(school, SCHOOL_FILE, disciplineComparator, studentComparator);
    System.out.println("Дані школи експортовано у файл " + SCHOOL_FILE);
  }

  private static void handleImportSchool() throws IOException {
    System.out.println("УВАГА: Ця операція замінить всі поточні дані школи!");
    System.out.print("Продовжити? (y/n): ");
    String confirmation = scanner.nextLine();
    if (!confirmation.trim().equalsIgnoreCase("y")) {
      System.out.println("Імпорт скасовано.");
      return;
    }

    try {
      School importedSchool = dataManager.importSchool(SCHOOL_FILE);
      school = importedSchool;
      System.out.println("Школу '" + school.getName() + "' успішно імпортовано з файлу " + SCHOOL_FILE);
      System.out.println("Імпортовано дисциплін: " + school.getDisciplines().size());
      System.out.println("Імпортовано студентів: " + school.getStudents().size());
      System.out
          .println("Увага: Оцінки студентів та зв'язки могли не відновитися коректно залежно від структури JSON.");
    } catch (IOException e) {
      System.err.println("Помилка імпорту школи з файлу " + SCHOOL_FILE + ": " + e.getMessage());
      System.err.println("Перевірте, чи існує файл і чи має він правильний формат.");
    }
  }
}
