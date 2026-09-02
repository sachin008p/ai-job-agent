package com.example.aijobagent.config;

import com.example.aijobagent.model.Job;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.JobRepository;
import com.example.aijobagent.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(JobRepository jobRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedJobs();
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setSkills("System Administration, Security, Java, Spring Boot");
            userRepository.save(admin);
            log.info("Seeded admin user: admin@example.com / admin123");
        }

        if (!userRepository.existsByEmail("user@example.com")) {
            User user = new User();
            user.setName("John Doe");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            user.setSkills("Java, Spring Boot, REST APIs, PostgreSQL, SQL, Maven, Git");
            userRepository.save(user);
            log.info("Seeded demo user: user@example.com / user123");
        }
    }

    private void seedJobs() {
        if (jobRepository.count() == 0) {
            Job job1 = new Job();
            job1.setTitle("Senior Java Developer");
            job1.setCompany("Acme Tech Solutions");
            job1.setLocation("Pune");
            job1.setTechnology("Java, Spring Boot, PostgreSQL");
            job1.setRequiredSkills("Java, Spring Boot, PostgreSQL, Microservices, REST APIs");
            job1.setJobType("Full-time");
            job1.setExperience("3-5 years");
            job1.setSalary("14-20 LPA");
            job1.setDescription("Build scalable backend microservices using Java 17, Spring Boot 3, Spring AI, PostgreSQL, and REST APIs.");
            jobRepository.save(job1);

            Job job2 = new Job();
            job2.setTitle("Full Stack Java Engineer");
            job2.setCompany("Innovate Digital");
            job2.setLocation("Pune");
            job2.setTechnology("Java, Spring Boot, React, SQL");
            job2.setRequiredSkills("Java, Spring Boot, React, JavaScript, SQL, PostgreSQL, HTML, CSS");
            job2.setJobType("Full-time");
            job2.setExperience("2-4 years");
            job2.setSalary("12-18 LPA");
            job2.setDescription("Develop responsive full-stack applications with Spring Boot backend services and React dynamic frontends.");
            jobRepository.save(job2);

            Job job3 = new Job();
            job3.setTitle("Backend Engineer (AI Integration)");
            job3.setCompany("FutureAI Systems");
            job3.setLocation("Remote");
            job3.setTechnology("Java, Spring AI, OpenAI API, Python");
            job3.setRequiredSkills("Java, Spring AI, OpenAI, Python, REST APIs, PostgreSQL, Docker");
            job3.setJobType("Remote");
            job3.setExperience("3-6 years");
            job3.setSalary("20-30 LPA");
            job3.setDescription("Integrate GenAI LLM function calling and agents into enterprise cloud applications.");
            jobRepository.save(job3);

            Job job4 = new Job();
            job4.setTitle("Junior Java Developer");
            job4.setCompany("TechStart Labs");
            job4.setLocation("Bangalore");
            job4.setTechnology("Java, Spring Boot, MySQL");
            job4.setRequiredSkills("Java, Spring Boot, SQL, Git, Maven");
            job4.setJobType("Full-time");
            job4.setExperience("0-2 years");
            job4.setSalary("6-10 LPA");
            job4.setDescription("Great entry level opportunity for passionate backend developers proficient in Java and SQL.");
            jobRepository.save(job4);

            log.info("Seeded initial sample jobs dataset into database.");
        }
    }
}
