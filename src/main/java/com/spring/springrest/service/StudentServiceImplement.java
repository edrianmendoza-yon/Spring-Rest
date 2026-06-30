package com.spring.springrest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.springrest.dto.StudentDto;
import com.spring.springrest.entity.Student;
import com.spring.springrest.exception.StudentNotFoundException;
import com.spring.springrest.mapper.StudentMapper;
import com.spring.springrest.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentServiceImplement implements StudentService {
    
    private StudentRepository studentRepository;
    
    public StudentServiceImplement(StudentRepository studentRepository) {
        //super();
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        // TODO Auto-generated method stub
        Student student = StudentMapper.toStudentEntity(studentDto);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.toStudentDto(savedStudent);
    }
    
    @Override
    public StudentDto getStudentBy(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        return StudentMapper.toStudentDto(student);
    }
    
    @Override
    public List<StudentDto> getAllStudents() {
        // TODO Auto-generated method stub
        List<Student> students = studentRepository.findAll();
        return students.stream().map(
               (student) -> StudentMapper.toStudentDto(student)).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + id));

        existingStudent.setFirstName(studentDto.getFirstName());
        existingStudent.setLastName(studentDto.getLastName());
        existingStudent.setEmail(studentDto.getEmail());

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentMapper.toStudentDto(updatedStudent);
    }
    
    @Override
    @Transactional
    public StudentDto patchStudent(Long id, StudentDto studentDto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + id));

        if (studentDto.getFirstName() != null) {
            existingStudent.setFirstName(studentDto.getFirstName());
        }
        if (studentDto.getLastName() != null) {
            existingStudent.setLastName(studentDto.getLastName());
        }
        if (studentDto.getEmail() != null) {
            existingStudent.setEmail(studentDto.getEmail());
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentMapper.toStudentDto(updatedStudent);
    }
    
    @Override
    @Transactional
    public void deleteStudentBy(Long id) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + id));
        studentRepository.delete(existingStudent);
    }
}