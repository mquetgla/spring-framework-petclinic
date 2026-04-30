package org.springframework.samples.petclinic.repository.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.repository.WeightRecordRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class JdbcWeightRecordRepositoryImpl implements WeightRecordRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert insertWeightRecord;

    public JdbcWeightRecordRepositoryImpl(DataSource dataSource, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.insertWeightRecord = new SimpleJdbcInsert(dataSource)
            .withTableName("weight_records")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public void save(WeightRecord weightRecord) {
        if (weightRecord.isNew()) {
            Number newKey = this.insertWeightRecord.executeAndReturnKey(
                createParameterSource(weightRecord));
            weightRecord.setId(newKey.intValue());
        } else {
            throw new UnsupportedOperationException("WeightRecord update not supported");
        }
    }

    private MapSqlParameterSource createParameterSource(WeightRecord weightRecord) {
        return new MapSqlParameterSource()
            .addValue("id", weightRecord.getId())
            .addValue("weight", weightRecord.getWeight())
            .addValue("measure_date", weightRecord.getMeasureDate())
            .addValue("pet_id", weightRecord.getPet().getId());
    }

    @Override
    public List<WeightRecord> findByPetId(Integer petId) {
        return this.jdbcClient
            .sql("SELECT id, weight, measure_date, pet_id FROM weight_records WHERE pet_id=:petId ORDER BY measure_date")
            .param("petId", petId)
            .query((rs, row) -> {
                WeightRecord wr = new WeightRecord();
                wr.setId(rs.getInt("id"));
                wr.setWeight(rs.getBigDecimal("weight"));
                wr.setMeasureDate(rs.getObject("measure_date", LocalDate.class));
                return wr;
            })
            .list();
    }
}
