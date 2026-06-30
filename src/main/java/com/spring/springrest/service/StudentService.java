package com.spring.springrest.service;

import java.util.List;

import com.spring.springrest.dto.StudentDto;

public interface StudentService {
    StudentDto createStudent(StudentDto studentDto);
    StudentDto getStudentBy(long id);
    List<StudentDto> getAllStudents();
    StudentDto updateStudent(Long id, StudentDto studentDto);
    StudentDto patchStudent(Long id, StudentDto studentDto);
    void deleteStudentBy(Long id);
}
