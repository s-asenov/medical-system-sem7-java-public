package com.medic.system.services;

import com.medic.system.dtos.drug.DrugRequestDto;
import com.medic.system.dtos.drug.EditDrugRequestDto;
import com.medic.system.entities.Drug;
import com.medic.system.repositories.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugService {
    private final DrugRepository drugRepository;

    public List<Drug> findAll() {
        return drugRepository.findAll();
    }

    public Page<Drug> findAll(Pageable pageable) {
        return drugRepository.findAll(pageable);
    }

    public Drug create(DrugRequestDto drugRequestDto, BindingResult bindingResult)
    {
        if (drugRequestDto == null) {
            bindingResult.rejectValue("name", "error.drug", "Грешка при създаване на лекарство");
            return null;
        }

        Drug drug = new Drug();
        drug.setName(drugRequestDto.getName());
        drug.setDescription(drugRequestDto.getDescription());
        drug.setPrice(drugRequestDto.getPrice());

        try {
            return drugRepository.save(drug);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.drug", "Грешка при създаване на лекарство");
            return null;
        }
    }

    public Drug update(Long id, EditDrugRequestDto editDrugRequestDto, BindingResult bindingResult) {
        if (editDrugRequestDto == null) {
            bindingResult.rejectValue("name", "error.drug", "Грешка при редактиране на лекарство");
            return null;
        }

        Drug drug;
        try {
            drug = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.drug", "Лекарството не съществува");
            return null;
        }

        drug.setName(editDrugRequestDto.getName());
        drug.setDescription(editDrugRequestDto.getDescription());
        drug.setPrice(editDrugRequestDto.getPrice());

        try {
            return drugRepository.save(drug);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.drug", "Грешка при редактиране на лекарство");
            return null;
        }
    }

    public Drug findById(Long id) {
        return drugRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) throws Exception {
        Drug drug = findById(id);
        if (drug.getMedicalAppointments().size() > 0) {
            throw new Exception("Лекарството се използва в медицински записи");
        }
        drugRepository.deleteById(id);
    }
}
