package com.ajaskiewicz.PlantManager.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FieldErrorResponse { //todo dodać timestamp

    private List<CustomFieldError> fieldErrors;

}
