package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;

public interface SpringDataWeightRecordRepository extends WeightRecordRepository, Repository<WeightRecord, Integer> {
}
