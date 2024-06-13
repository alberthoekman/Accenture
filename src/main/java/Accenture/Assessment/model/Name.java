package Accenture.Assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a name object. The API returns multiple names, but only one is needed for our
 * purposes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Name(String common) {}
