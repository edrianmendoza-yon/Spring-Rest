package com.spring.springrest.mapper;

import com.spring.springrest.dto.StudentDto;
import com.spring.springrest.entity.Student;

public class StudentMapper {
    public static StudentDto toStudentDto(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentDto(student.getId(), student.getFirstName(),
                student.getLastName(), student.getEmail());
    }
    
    public static Student toStudentEntity(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        return student;
    }
}
