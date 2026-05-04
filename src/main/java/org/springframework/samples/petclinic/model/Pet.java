/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
    private Set<Visit> visits;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "microchip_id", unique = true)
    private String microchip;

    @Column(name = "color")
    private String color;

    @Column(name = "breed")
    private String breed;

	@Column(name = "active")
	private Boolean active = true;

	@Column(name = "weight", precision = 5, scale = 2)
	@DecimalMin(value = "0.01", message = "Weight must be a positive number")
	private BigDecimal weight;

	@Lob
	@Column(name = "notes")
	private String notes;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender = Gender.UNKNOWN;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
    private Set<WeightRecord> weightRecords;

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public PetType getType() {
        return this.type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMicrochip() {
        return this.microchip;
    }

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreed() {
        return this.breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
		this.active = active;
	}

	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	protected Set<Visit> getVisitsInternal() {
        if (this.visits == null) {
            this.visits = new HashSet<>();
        }
        return this.visits;
    }

    protected void setVisitsInternal(Set<Visit> visits) {
        this.visits = visits;
    }

    public List<Visit> getVisits() {
        List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
        sortedVisits.sort(Comparator.comparing(Visit::getDate).reversed());
        return Collections.unmodifiableList(sortedVisits);
    }

    public void addVisit(Visit visit) {
        getVisitsInternal().add(visit);
        visit.setPet(this);
    }

    protected Set<WeightRecord> getWeightRecordsInternal() {
        if (this.weightRecords == null) {
            this.weightRecords = new HashSet<>();
        }
        return this.weightRecords;
    }

    protected void setWeightRecordsInternal(Set<WeightRecord> weightRecords) {
        this.weightRecords = weightRecords;
    }

    public List<WeightRecord> getWeightRecords() {
        List<WeightRecord> sortedRecords = new ArrayList<>(getWeightRecordsInternal());
        sortedRecords.sort(Comparator.comparing(WeightRecord::getMeasureDate));
        return Collections.unmodifiableList(sortedRecords);
    }

    public void addWeightRecord(WeightRecord weightRecord) {
        getWeightRecordsInternal().add(weightRecord);
        weightRecord.setPet(this);
    }

}
