package Accenture.Assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** Represents a country. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Country(
    Name name, String region, double area, int population, String cca3, List<String> borders) {

  /**
   * Returns the population density based on population and area.
   *
   * @return the population density.
   */
  public int density() {
    return (int) (population / area);
  }
}
