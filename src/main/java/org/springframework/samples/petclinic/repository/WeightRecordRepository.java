package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.samples.petclinic.model.WeightRecord;

public interface WeightRecordRepository {

    void save(WeightRecord weightRecord);

    List<WeightRecord> findByPetId(Integer petId);
}
