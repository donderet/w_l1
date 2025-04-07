package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataManagerTest {

  @Mock
  private ObjectMapper mockObjectMapper;

  @InjectMocks
  private DataManager dataManager;

  private List<Discipline> testDisciplines;
  private List<Student> testStudents;
  private School testSchool;
  private String testFilename = "test_data.json";
  private File testFile;

  @Captor
  private ArgumentCaptor<File> fileCaptor;
  @Captor
  private ArgumentCaptor<Object> dataCaptor;
  @Captor
  private ArgumentCaptor<TypeReference<?>> typeRefCaptor;

  @BeforeEach
  void setUp() {

    Discipline math = new Discipline("Математика");
    Discipline physics = new Discipline("Фізика");
    testDisciplines = new ArrayList<>(List.of(physics, math));
    Student student1 = new Student("Іван", "А");
    Student student2 = new Student("Петро", "Б");
    student1.addGrade(math, 10);
    student2.addGrade(physics, 9);
    testStudents = new ArrayList<>(List.of(student2, student1));
    testSchool = new School("Тестова Школа Імпорт/Експорт");
    testSchool.addDiscipline(math);
    testSchool.addDiscipline(physics);
    testSchool.addStudent(student1);
    testSchool.addStudent(student2);

    testFile = new File(testFilename);
  }

  @Test
  @DisplayName("Експорт дисциплін: сортує та викликає writeValue")
  void exportDisciplines_SortsAndWrites() throws IOException {
    Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

    dataManager.exportDisciplines(testDisciplines, testFilename, comparator);

    verify(mockObjectMapper, times(1)).writeValue(fileCaptor.capture(), dataCaptor.capture());

    assertEquals(testFile, fileCaptor.getValue());

    assertTrue(dataCaptor.getValue() instanceof List);
    @SuppressWarnings("unchecked")
    List<Discipline> capturedList = (List<Discipline>) dataCaptor.getValue();
    assertEquals(2, capturedList.size());
    assertEquals("Математика", capturedList.get(0).getName());
    assertEquals("Фізика", capturedList.get(1).getName());
  }

  @Test
  @DisplayName("Експорт студентів: сортує та викликає writeValue")
  void exportStudents_SortsAndWrites() throws IOException {
    Comparator<Student> comparator = Comparator.comparing(Student::getLastName);

    dataManager.exportStudents(testStudents, testFilename, comparator);

    verify(mockObjectMapper, times(1)).writeValue(fileCaptor.capture(), dataCaptor.capture());
    assertEquals(testFile, fileCaptor.getValue());
    assertTrue(dataCaptor.getValue() instanceof List);
    @SuppressWarnings("unchecked")
    List<Student> capturedList = (List<Student>) dataCaptor.getValue();
    assertEquals(2, capturedList.size());
    assertEquals("А", capturedList.get(0).getLastName());
    assertEquals("Б", capturedList.get(1).getLastName());
  }

  @Test
  @DisplayName("Експорт школи: сортує списки та викликає writeValue з Map")
  void exportSchool_SortsAndWritesMap() throws IOException {
    Comparator<Discipline> disciplineComparator = Comparator.comparing(Discipline::getName);
    Comparator<Student> studentComparator = Comparator.comparing(Student::getLastName);

    dataManager.exportSchool(testSchool, testFilename, disciplineComparator, studentComparator);

    verify(mockObjectMapper, times(1)).writeValue(fileCaptor.capture(), dataCaptor.capture());
    assertEquals(testFile, fileCaptor.getValue());
    assertTrue(dataCaptor.getValue() instanceof Map);

    @SuppressWarnings("unchecked")
    Map<String, Object> capturedMap = (Map<String, Object>) dataCaptor.getValue();
    assertEquals(testSchool.getName(), capturedMap.get("name"));

    assertTrue(capturedMap.get("disciplines") instanceof List);
    @SuppressWarnings("unchecked")
    List<Discipline> capturedDisciplines = (List<Discipline>) capturedMap.get("disciplines");
    assertEquals(2, capturedDisciplines.size());
    assertEquals("Математика", capturedDisciplines.get(0).getName());
    assertEquals("Фізика", capturedDisciplines.get(1).getName());

    assertTrue(capturedMap.get("students") instanceof List);
    @SuppressWarnings("unchecked")
    List<Student> capturedStudents = (List<Student>) capturedMap.get("students");
    assertEquals(2, capturedStudents.size());
    assertEquals("А", capturedStudents.get(0).getLastName());
    assertEquals("Б", capturedStudents.get(1).getLastName());
  }

  @Test
  @DisplayName("Експорт: ObjectMapper кидає IOException")
  void export_ObjectMapperThrowsIOException() throws IOException {
    doThrow(new IOException("Помилка запису (тест)"))
        .when(mockObjectMapper).writeValue(any(File.class), any());

    Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

    assertThrows(IOException.class, () -> dataManager.exportDisciplines(testDisciplines, testFilename, comparator));
  }

  @Test
  @DisplayName("Імпорт дисциплін: викликає readValue та повертає список")
  void importDisciplines_ReadsAndReturns() throws IOException {
    List<Discipline> expectedDisciplines = List.of(new Discipline("Хімія"), new Discipline("Біологія"));

    when(mockObjectMapper.readValue(eq(testFile), any(TypeReference.class)))
        .thenReturn(expectedDisciplines);

    List<Discipline> actualDisciplines = dataManager.importDisciplines(testFilename);

    assertEquals(expectedDisciplines, actualDisciplines);
    verify(mockObjectMapper, times(1)).readValue(eq(testFile), any(TypeReference.class));
  }

  @Test
  @DisplayName("Імпорт студентів: викликає readValue та повертає список")
  void importStudents_ReadsAndReturns() throws IOException {
    List<Student> expectedStudents = List.of(new Student("Студ", "Імп1"), new Student("Студ", "Імп2"));
    when(mockObjectMapper.readValue(eq(testFile), any(TypeReference.class)))
        .thenReturn(expectedStudents);

    List<Student> actualStudents = dataManager.importStudents(testFilename);

    assertEquals(expectedStudents, actualStudents);
    verify(mockObjectMapper, times(1)).readValue(eq(testFile), any(TypeReference.class));
  }
}
