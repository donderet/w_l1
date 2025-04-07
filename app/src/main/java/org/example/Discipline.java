package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Discipline {
  private String name;

  @JsonCreator
  public Discipline(@JsonProperty("name") String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Назва дисципліни не може бути порожньою.");
    }
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Назва дисципліни не може бути порожньою.");
    }
    this.name = name;
  }

  @Override
  public String toString() {
    return "Дисципліна '" + name + '\'';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Discipline that = (Discipline) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
