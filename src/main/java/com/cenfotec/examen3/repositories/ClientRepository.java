package com.cenfotec.examen3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cenfotec.examen3.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
