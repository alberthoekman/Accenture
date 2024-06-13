package Accenture.Assessment.service;

import Accenture.Assessment.config.ApiConfig;
import Accenture.Assessment.exception.CustomException;
import Accenture.Assessment.model.Country;
import Accenture.Assessment.model.CountryWithNeighbours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/** Service class for managing country data. */
@Service
public class CountryService {

  @Autowired private RestTemplate restTemplate;

  @Autowired private ApiConfig apiConfig;

  private HashMap<String, HashMap<String, Country>> nestedCountryMap;
  private HashMap<String, Country> countryMap;

  /**
   * Loads country data from an array into internal maps for quick lookup.
   *
   * @param countries an array of Country objects to be loaded into maps.
   */
  private void loadData(Country[] countries) {
    // Initialize nested map for computation and flat map for quick lookup.
    nestedCountryMap = new HashMap<>();
    countryMap = new HashMap<>();

    // Populate maps.
    for (Country country : countries) {
      nestedCountryMap
          .computeIfAbsent(country.region(), r -> new HashMap<>())
          .put(country.cca3(), country);
      countryMap.put(country.cca3(), country);
    }
  }

  /**
   * Calls the external API for a list of all countries.
   *
   * @throws CustomException when the API did not return any countries.
   * @return an array of Country objects.
   */
  public Country[] getAllCountries() {
    // Construct URL and make the API call.
    String url = apiConfig.getBaseUrl() + apiConfig.getAllCountriesEndpoint();
    Country[] countries = restTemplate.getForObject(url, Country[].class);

    if (countries == null) {
      throw new CustomException(
          "Something went wrong.",
          HttpStatus.INTERNAL_SERVER_ERROR,
          "API did not return any countries");
    }

    return countries;
  }

  /**
   * Returns an array of Country objects sorted by population density descending.
   *
   * @return sorted array of Country objects.
   */
  public Country[] countriesByPopulationDensity() {
    Country[] countries = getAllCountries();

    // Sort the array of countries by density descending.
    Arrays.sort(countries, Comparator.comparing(Country::density).reversed());

    return countries;
  }

  /**
   * Returns the countries in a specified region with the most neighbouring countries in a different
   * region.
   *
   * @param region the region in which the original country is to be located.
   * @throws CustomException when an invalid region is supplied.
   * @return a List with CountryWithNeighbours objects.
   */
  public List<CountryWithNeighbours> countriesWithMostNeighboursInDifferentRegion(String region) {
    Country[] countries = getAllCountries();
    loadData(countries);

    // Initialize local variables.
    int most = 0;
    List<Country> mostCountries = new ArrayList<>();
    HashMap<String, Country> regionMap = nestedCountryMap.get(region);

    if (regionMap == null) {
      throw new CustomException(
          "Invalid region.", HttpStatus.NOT_FOUND, "Nullpointer: regionMap was not found.");
    }

    // Loop over each country in the region in question.
    for (Country country : regionMap.values()) {
      // If the country has no neighbours, continue to the next.
      if (country.borders() == null) {
        continue;
      }

      int counter = 0;

      // Check each bordering country if it is in the same region. If not, increment the counter.
      for (String cca : country.borders()) {
        if (!regionMap.containsKey(cca)) {
          counter++;
        }
      }

      if (counter > most) {
        // If the counter exceeds the current most counter, then this is the new winner.
        // Increment the counter and reset the list of winners.
        most = counter;
        mostCountries.clear();
        mostCountries.add(country);
      } else if (counter == most) {
        // If the counter is the same, add to the list of winners.
        mostCountries.add(country);
      }
    }

    // Make a list of CountryWithNeighbours for use in the view later.
    List<CountryWithNeighbours> result = new ArrayList<>();

    for (Country country : mostCountries) {
      List<Country> neighbours = new ArrayList<>();

      for (String code : country.borders()) {
        if (!regionMap.containsKey(code)) {
          Country neighbour = countryMap.get(code);
          neighbours.add(neighbour);
        }
      }

      result.add(new CountryWithNeighbours(country, neighbours));
    }

    return result;
  }
}
