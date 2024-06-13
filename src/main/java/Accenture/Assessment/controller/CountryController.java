package Accenture.Assessment.controller;

import Accenture.Assessment.model.Country;
import Accenture.Assessment.model.CountryWithNeighbours;
import Accenture.Assessment.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** Controller for managing /country requests. */
@Controller
@RequestMapping("/country")
public class CountryController {

  @Autowired private CountryService countryService;

  /**
   * Retrieves a list of all countries sorted by population density descending.
   *
   * @param model to send variables to Thymeleaf.
   * @return a string for Thymeleaf to find the correct template.
   */
  @GetMapping("/density")
  public String getDensities(Model model) {
    Country[] countries = countryService.countriesByPopulationDensity();
    model.addAttribute("countries", countries);
    return "densities";
  }

  /**
   * Retrieves a list of countries in a specific region with the most neighboring countries in a
   * different region.
   *
   * @param region the region to search within.
   * @param model to send variables to Thymeleaf.
   * @return a string for Thymeleaf to find the correct template.
   */
  @GetMapping("/neighbours")
  public String getMostNeighbours(@RequestParam(defaultValue = "Asia") String region, Model model) {

    List<CountryWithNeighbours> countries =
        countryService.countriesWithMostNeighboursInDifferentRegion(region);
    model.addAttribute("countries", countries);
    model.addAttribute("region", region);
    model.addAttribute("most", countries.getFirst().getNeighbours().size());

    return "neighbours";
  }
}
