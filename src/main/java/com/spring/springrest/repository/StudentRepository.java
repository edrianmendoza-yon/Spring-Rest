package com.spring.springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.springrest.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
