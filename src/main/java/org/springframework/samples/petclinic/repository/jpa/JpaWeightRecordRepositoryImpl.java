package org.springframework.samples.petclinic.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final EntityManager em;

    public JpaWeightRecordRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.getId() == null) {
            this.em.persist(weightRecord);
        } else {
            this.em.merge(weightRecord);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WeightRecord> findByPetId(Integer petId) {
        Query query = this.em.createQuery("SELECT wr FROM WeightRecord wr WHERE wr.pet.id = :petId ORDER BY wr.measureDate");
        query.setParameter("petId", petId);
        return query.getResultList();
    }
}
