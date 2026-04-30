package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weight_records")
public class WeightRecord extends BaseEntity {

    @Column(name = "weight", precision = 5, scale = 2)
    @NotNull
    @DecimalMin(value = "0.01", message = "Weight must be a positive number")
    private BigDecimal weight;

    @Column(name = "measure_date")
    @NotNull
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate measureDate;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public WeightRecord() {
        this.measureDate = LocalDate.now();
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public LocalDate getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(LocalDate measureDate) {
        this.measureDate = measureDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
