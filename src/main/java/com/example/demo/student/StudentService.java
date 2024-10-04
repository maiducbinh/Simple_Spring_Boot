package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
//        System.out.println(student);
            Optional<Student> optional = studentRepository.findStudentByEmail(student.getEmail());
            if (optional.isPresent()) {
                throw new IllegalStateException("Student already exists");
            }
            studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Student does not exist");
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new IllegalStateException("Student does not exist");
        }
        if(name != null && !name.isEmpty() && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if(email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> optional = studentRepository.findStudentByEmail(email);
            if (optional.isPresent()) {
                throw new IllegalStateException("Student already exists");
            }
            student.setEmail(email);
        }
    }
}
