package com.medic.system.services;

public interface BaseService<DTO, T> {
    T save(DTO dto);

}
