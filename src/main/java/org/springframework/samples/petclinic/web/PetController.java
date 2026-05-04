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
package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collection;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    private final ClinicService clinicService;

    public PetController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return this.clinicService.findPetTypes();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int ownerId) {
        return this.clinicService.findOwnerById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("pet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PetValidator());
    }

    @GetMapping(value = "/pets/new")
    public String initCreationForm(Owner owner, ModelMap model) {
        Pet pet = new Pet();
        owner.addPet(pet);
        model.put("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        }

        owner.addPet(pet);
        this.clinicService.savePet(pet);
        return "redirect:/owners/{ownerId}";
    }

    @GetMapping(value = "/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
        Pet pet = this.clinicService.findPetById(petId);
        model.put("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/pets/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model) {
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        }

        owner.addPet(pet);
        this.clinicService.savePet(pet);
        return "redirect:/owners/{ownerId}";
    }

    @GetMapping("/pets/{petId}/transfer")
    public String initTransferPetForm(@PathVariable("petId") int petId, Owner owner, ModelMap model) {
        Pet pet = this.clinicService.findPetById(petId);
        model.put("pet", pet);
        return "pets/transferPetForm";
    }

    @GetMapping("/pets/{petId}/transfer/search")
    public String searchOwnersForTransfer(@PathVariable("petId") int petId, @RequestParam(value = "lastName", required = false) String lastName, ModelMap model) {
        Pet pet = this.clinicService.findPetById(petId);
        model.put("pet", pet);
        if (StringUtils.hasText(lastName)) {
            Collection<Owner> results = this.clinicService.findOwnerByLastName(lastName);
            model.put("results", results);
        }
        return "pets/transferPetForm";
    }

    @PostMapping("/pets/{petId}/transfer")
    public String processPetTransfer(@PathVariable("petId") int petId, @RequestParam("newOwnerId") int newOwnerId,
                                    @RequestParam(value = "confirm", required = false) String confirm,
                                    Owner owner, ModelMap model) {
        Pet pet = this.clinicService.findPetById(petId);
        Owner newOwner = this.clinicService.findOwnerById(newOwnerId);

        if (newOwner == null) {
            model.put("pet", pet);
            model.put("errorMessage", "Selected owner does not exist.");
            return "pets/transferPetForm";
        }

        if (pet.getOwner().getId() == newOwnerId) {
            model.put("pet", pet);
            model.put("errorMessage", "Cannot transfer pet to the same owner.");
            return "pets/transferPetForm";
        }

        if (confirm == null || !confirm.equals("true")) {
            model.put("pet", pet);
            model.put("newOwner", newOwner);
            model.put("showConfirmation", true);
            return "pets/transferPetForm";
        }

        this.clinicService.transferPetToOwner(petId, newOwnerId);
        pet = this.clinicService.findPetById(petId);
        model.put("pet", pet);
        model.put("newOwner", newOwner);
        model.put("successMessage", "Pet ownership has been successfully transferred.");
        return "pets/transferPetSuccess";
    }

}
