package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.WeightRecord;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class WeightRecordController {

    private final ClinicService clinicService;

    public WeightRecordController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("weightRecord")
    public WeightRecord loadPetWithWeightRecord(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        WeightRecord weightRecord = new WeightRecord();
        weightRecord.setPet(pet);
        return weightRecord;
    }

    @GetMapping(value = "/owners/{ownerId}/pets/{petId}/weightRecords/new")
    public String initNewWeightRecordForm(@PathVariable("petId") int petId, Map<String, Object> model) {
        return "pets/createOrUpdateWeightRecordForm";
    }

    @PostMapping(value = "/owners/{ownerId}/pets/{petId}/weightRecords/new")
    public String processNewWeightRecordForm(@Valid WeightRecord weightRecord, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateWeightRecordForm";
        }

        this.clinicService.saveWeightRecord(weightRecord);
        return "redirect:/owners/{ownerId}";
    }
}
