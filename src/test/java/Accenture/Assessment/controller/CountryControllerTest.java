package Accenture.Assessment.controller;

import Accenture.Assessment.model.Country;
import Accenture.Assessment.model.CountryWithNeighbours;
import Accenture.Assessment.model.Name;
import Accenture.Assessment.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Unit tests for the CountryController class. */
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

  @MockBean
  private CountryService countryService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void getDensities_success() throws Exception {
    Country country1 =
        new Country(new Name("Netherlands"), "Europe", 3, 10, "NL", List.of("GER", "BEL"));
    Country country2 = new Country(new Name("Belgium"), "Europe", 2, 10, "BEL", List.of("NL"));
    Country country3 = new Country(new Name("Germany"), "Europe", 1, 10, "GER", List.of("NL"));
    Country[] countries = {country1, country2, country3};

    when(countryService.countriesByPopulationDensity()).thenReturn(countries);

    mockMvc
        .perform(get("/country/density"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("countries"));
  }

  /**
   * Unit test for the getCountriesWithMostForeignNeighbors function.
   *
   * <p>This test checks if the HttpStatus = OK and if the correct data is being sent to the
   * Thymeleaf template.
   */
  @Test
  void getCountriesWithMostForeignNeighbors_success() throws Exception {
    Country country =
        new Country(
            new Name("Netherlands"),
            "Kingdom of the Netherlands",
            10,
            10,
            "NL",
            List.of("GER", "BEL"));
    Country neighbour1 =
        new Country(new Name("Belgium"), "Europe", 10, 10, "BEL", List.of("NL", "GER"));
    Country neighbour2 =
        new Country(new Name("Germany"), "Europe", 10, 10, "GER", List.of("NL", "BEL"));

    when(countryService.countriesWithMostNeighboursInDifferentRegion("Europe"))
        .thenReturn(List.of(new CountryWithNeighbours(country, List.of(neighbour1, neighbour2))));

    mockMvc
        .perform(get("/country/neighbours").param("region", "Europe"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("countries"));
  }
}
