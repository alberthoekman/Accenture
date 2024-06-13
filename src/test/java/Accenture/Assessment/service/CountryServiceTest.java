package Accenture.Assessment.service;

import Accenture.Assessment.config.ApiConfig;
import Accenture.Assessment.exception.CustomException;
import Accenture.Assessment.model.Country;
import Accenture.Assessment.model.CountryWithNeighbours;
import Accenture.Assessment.model.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Unit tests for the CountryService class. */
public class CountryServiceTest {
  @Mock private ApiConfig apiConfig;

  @Mock private RestTemplate restTemplate;

  @InjectMocks private CountryService countryService;

  @Spy private CountryService spyCountryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    spyCountryService = spy(countryService);
  }

  /**
   * Unit test for the success case for the getAllCountries function.
   *
   * <p>This test verifies if the correct amount of countries is returned.
   */
  @Test
  void getAllCountries_success() {
    Country country1 =
        new Country(
            new Name("Netherlands"),
            "Kingdom of the Netherlands",
            10,
            10,
            "NL",
            List.of("GER", "BEL"));
    Country country2 = new Country(new Name("Belgium"), "Europe1", 10, 10, "BEL", List.of("NL"));
    Country country3 = new Country(new Name("Germany"), "Europe2", 10, 10, "GER", List.of("NL"));
    Country[] countries = {country1, country2, country3};

    when(apiConfig.getBaseUrl()).thenReturn("https://mock.com");
    when(apiConfig.getAllCountriesEndpoint()).thenReturn("/all");
    when(restTemplate.getForObject("https://mock.com/all", Country[].class)).thenReturn(countries);

    Country[] result = countryService.getAllCountries();

    assertEquals(3, result.length);
  }

  /**
   * Unit test for the failure case for the getAllCountries function.
   *
   * <p>This test verifies that an exception is thrown when no countries are returned.
   */
  @Test
  void getAllCountries_failure() {
    when(apiConfig.getBaseUrl()).thenReturn("https://mock.com");
    when(apiConfig.getAllCountriesEndpoint()).thenReturn("/all");
    when(restTemplate.getForObject("https://mock.com/all", Country[].class)).thenReturn(null);

    assertThrows(CustomException.class, () -> countryService.getAllCountries());
  }

  /**
   * Unit test that tests the success case for the countriesByPopulationDensity function.
   *
   * <p>This test checks if the countries are in the correct order.
   */
  @Test
  void countriesByPopulationDensity_success() {
    Country country =
        new Country(new Name("Netherlands"), "Europe", 3, 10, "NL", List.of("GER", "BEL"));
    Country country2 = new Country(new Name("Belgium"), "Europe", 2, 10, "BEL", List.of("NL"));
    Country country3 = new Country(new Name("Germany"), "Europe", 1, 10, "GER", List.of("NL"));

    doReturn(new Country[] {country, country2, country3}).when(spyCountryService).getAllCountries();
    Country[] sortedCountries = spyCountryService.countriesByPopulationDensity();

    assertEquals("GER", sortedCountries[0].cca3());
    assertEquals("BEL", sortedCountries[1].cca3());
    assertEquals("NL", sortedCountries[2].cca3());
  }

  /**
   * Unit test for the success case for the countriesWithMostNeighboursInDifferentRegion function
   * when one country is to be returned.
   *
   * <p>This test verifies that the correct country is returned.
   */
  @Test
  void countriesWithMostNeighboursInDifferentRegion_success_single() {
    Country country =
        new Country(
            new Name("Netherlands"),
            "Kingdom of the Netherlands",
            10,
            10,
            "NL",
            List.of("GER", "BEL"));
    Country neighbour1 = new Country(new Name("Belgium"), "Europe1", 10, 10, "BEL", List.of("NL"));
    Country neighbour2 = new Country(new Name("Germany"), "Europe2", 10, 10, "GER", List.of("NL"));

    doReturn(new Country[] {country, neighbour1, neighbour2})
        .when(spyCountryService)
        .getAllCountries();
    List<CountryWithNeighbours> result =
        spyCountryService.countriesWithMostNeighboursInDifferentRegion(
            "Kingdom of the Netherlands");

    assertEquals(1, result.size());
    assertEquals("NL", result.getFirst().getCountry().cca3());
  }

  /**
   * Unit test for the success case for the countriesWithMostNeighboursInDifferentRegion function
   * when multiple countries are to be returned.
   *
   * <p>This test verifies that the correct countries are returned.
   */
  @Test
  void countriesWithMostNeighboursInDifferentRegion_success_multiple() {
    Country country =
        new Country(
            new Name("Netherlands"),
            "Kingdom of the Netherlands",
            10,
            10,
            "NL",
            List.of("GER", "BEL"));
    Country country2 =
        new Country(
            new Name("Friesland"),
            "Kingdom of the Netherlands",
            10,
            10,
            "FR",
            List.of("GER", "BEL"));
    Country neighbour1 = new Country(new Name("Belgium"), "Europe1", 10, 10, "BEL", List.of("NL"));
    Country neighbour2 = new Country(new Name("Germany"), "Europe2", 10, 10, "GER", List.of("NL"));

    doReturn(new Country[] {country, country2, neighbour1, neighbour2})
        .when(spyCountryService)
        .getAllCountries();
    List<CountryWithNeighbours> result =
        spyCountryService.countriesWithMostNeighboursInDifferentRegion(
            "Kingdom of the Netherlands");

    List<String> valid = List.of("NL", "FR");

    assertEquals(2, result.size());
    assertTrue(valid.contains(result.getFirst().getCountry().cca3()));
    assertTrue(valid.contains(result.get(1).getCountry().cca3()));
  }

  /**
   * Unit test for the failure case for the countriesWithMostNeighboursInDifferentRegion function.
   *
   * <p>This test verifies the an exception in thrown when an incorrect region is supplied.
   */
  @Test
  void countriesWithMostNeighboursInDifferentRegion_failure() {
    doReturn(new Country[] {}).when(spyCountryService).getAllCountries();
    assertThrows(
        CustomException.class,
        () -> spyCountryService.countriesWithMostNeighboursInDifferentRegion(""));
  }
}
