package org.sv.exceltopojo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model representing an Iris flower record")
public class Iris {

    @Schema(description = "Sepal length in centimeters", example = "5.1")
    private Double sepalLength;

    @Schema(description = "Sepal width in centimeters", example = "3.5")
    private Double sepalWidth;

    @Schema(description = "Petal length in centimeters", example = "1.4")
    private Double petalLength;

    @Schema(description = "Petal width in centimeters", example = "0.2")
    private Double petalWidth;

    @Schema(description = "Species name", example = "setosa")
    private String species;

    public Iris() {}

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String species) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.species = species;
    }

}
