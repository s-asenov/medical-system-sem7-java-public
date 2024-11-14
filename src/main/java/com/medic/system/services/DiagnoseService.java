package com.medic.system.services;

import com.medic.system.dtos.diagnose.EditDiagnoseRequestDto;
import com.medic.system.dtos.diagnose.DiagnoseRequestDto;
import com.medic.system.entities.Diagnose;
import com.medic.system.repositories.DiagnoseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnoseService {
    private final DiagnoseRepository diagnoseRepository;

    public List<Diagnose> findAllOrderedByName() {
        return diagnoseRepository.findAllByOrderByName();
    }

    public List<Diagnose> findAll() {
        return diagnoseRepository.findAll();
    }

    public Page<Diagnose> findAll(Pageable pageable) {
        return diagnoseRepository.findAll(pageable);
    }

    public Diagnose create(DiagnoseRequestDto diagnoseRequestDto, BindingResult bindingResult)
    {
        if (diagnoseRequestDto == null) {
            bindingResult.rejectValue("name", "error.diagnose", "Грешка при създаване на диагноза");
            return null;
        }

        Diagnose diagnose = new Diagnose();
        diagnose.setName(diagnoseRequestDto.getName());
        diagnose.setDescription(diagnoseRequestDto.getDescription());

        try {
            return diagnoseRepository.save(diagnose);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.diagnose", "Грешка при създаване на диагноза");
            return null;
        }
    }

    public Diagnose update(Long id, EditDiagnoseRequestDto editDiagnoseRequestDto, BindingResult bindingResult) {
        if (editDiagnoseRequestDto == null) {
            bindingResult.rejectValue("name", "error.diagnose", "Грешка при редактиране на диагноза");
            return null;
        }

        Diagnose diagnose;
        try {
            diagnose = findById(id);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.diagnose", "Диагнозата не съществува");
            return null;
        }

        diagnose.setName(editDiagnoseRequestDto.getName());
        diagnose.setDescription(editDiagnoseRequestDto.getDescription());

        try {
            return diagnoseRepository.save(diagnose);
        } catch (Exception e) {
            bindingResult.rejectValue("name", "error.diagnose", "Грешка при редактиране на диагноза");
            return null;
        }
    }

    public Diagnose findById(Long id) {
        return diagnoseRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        diagnoseRepository.deleteById(id);
    }
}
