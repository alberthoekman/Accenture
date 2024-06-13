package Accenture.Assessment.model;

import java.util.List;

/** Wrapper class for a Country with its neighbours as Country objects. */
public class CountryWithNeighbours {
  private Country country;
  private List<Country> neighbours;

  public CountryWithNeighbours(Country country, List<Country> neighbours) {
    this.country = country;
    this.neighbours = neighbours;
  }

  public Country getCountry() {
    return country;
  }

  public List<Country> getNeighbours() {
    return neighbours;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public void setNeighbours(List<Country> neighbors) {
    this.neighbours = neighbors;
  }
}
