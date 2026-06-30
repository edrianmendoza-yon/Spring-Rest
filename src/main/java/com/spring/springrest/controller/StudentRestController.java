package com.spring.springrest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springrest.dto.StudentDto;
import com.spring.springrest.entity.Student;
import com.spring.springrest.exception.StudentNotFoundException;
import com.spring.springrest.service.StudentService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {
    private List<Student> students;

    private StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    static Long studentId = 0L;

    @PostConstruct
    public void initialize() {
        
          students = new ArrayList<Student>(); students.add(new
          Student(studentId++, "John", "Doe", "johndoe@example.com"));
          students.add(new Student(studentId++, "Mary", "Public",
          "mary@public.com")); students.add(new Student(studentId++,
          "Mary Grace", "Piattos", "marygrace@piattos.com"));
         
        /*
         * studentService.createStudent(new StudentDto(studentId++, "John",
         * "Doe", "johndoe@example.com")); studentService.createStudent(new
         * StudentDto(studentId++, "Mary", "Public", "mary@public.com"));
         * studentService.createStudent(new StudentDto(studentId++,
         * "Mary Grace", "Piattos", "marygrace@piattos.com"));
         */
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId) {

        if (studentId >= students.size() || studentId < 0) {
            throw new StudentNotFoundException(
                    "Student id not found - " + studentId);
        }

        return students.get(studentId);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(
            @Valid @RequestBody StudentDto studentDto) {
        StudentDto savedStudentDto = studentService.createStudent(studentDto);
        return new ResponseEntity<StudentDto>(savedStudentDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentBy(@PathVariable long id) {
        StudentDto studentDto = studentService.getStudentBy(id);
        // return new ResponseEntity<>(studentDto, HttpStatus.OK);
        return ResponseEntity.ok(studentDto);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id,
            @Valid @RequestBody StudentDto studentDto) {
        StudentDto updatedStudent = studentService.updateStudent(id,
                studentDto);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<StudentDto> patchStudent(@PathVariable Long id,
            @Valid @RequestBody StudentDto studentDto) {
        StudentDto updatedStudent = studentService.patchStudent(id, studentDto);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentBy(@PathVariable Long id) {
        studentService.deleteStudentBy(id);
        return ResponseEntity.ok("Student with id " + id + " has been deleted successfully.");
    }
}
