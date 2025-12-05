

/java/org.example/config/OpenApiConfig.java
```java
package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.version}")
    private String version;

    @Value("${spring.application.name:nome}")
    private String appName;

    //http://localhost:8081/swagger-ui/index.html
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info().title(appName)
                        .description("API service in Swagger framework")
                        .version(version)
                );
    }
}

```


/java/org.example/controller/AlmnoController.java
```java
package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.model.Alumno;
import org.example.repository.AlumnoRepository;
import org.example.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(AlumnoController.MAPPING)
public class AlumnoController {

    public static final String MAPPING = "/alumno";

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoService alumnoServices;

    @Operation(summary = "Método que saúda")
    @PostMapping("/saudo")
    public String saudo() {
        return "Boas";
    }

    @Operation(summary = "Crear un nuevo alumno")
    @PostMapping("/")
    public Alumno crearAlumno(@RequestBody Alumno alumno) {
        return alumnoServices.crearAlumno(alumno);
    }

    @Operation(summary = "Obtener todos los alumnos")
    @GetMapping("/")
    public List<Alumno> obtenerAlumnos() {
        return alumnoServices.obterTodoslosAlumnos();
    }

    @Operation(summary = "Obtener alumno por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable Long id) {
        Optional<Alumno> alumno = alumnoServices.obterPersoaPorId(id);
        return alumno.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un alumno")
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumnoDetails) {
        Optional<Alumno> alumnoOptional = alumnoServices.obterPersoaPorId(id);
        if (alumnoOptional.isPresent()) {
            Alumno alumno = alumnoOptional.get();
            alumno.setNome(alumnoDetails.getNome());
            alumno.setApelidos(alumnoDetails.getApelidos()); // Actualizar apellidos
            if (alumnoDetails.getTitor() != null) {
                alumno.setTitor(alumnoDetails.getTitor()); // Actualizar titor si se proporciona
            }

            Alumno alumnoActualizado = alumnoServices.crearAlumno(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un alumno")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
        if (alumnoRepository.existsById(id)) {
            alumnoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```

/java/org.example/controller/TitorController.java
```java
package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.model.Titor;
import org.example.repository.TitorRepository;
import org.example.service.TitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(TitorController.MAPPING)
public class TitorController {

    public static final String MAPPING = "/titor";

    @Autowired
    private TitorRepository titorRepository;

    @Autowired
    private TitorService titorService;

    @Operation(summary = "Método que saúda")
    @PostMapping("/saudo")
    public String saudo() {
        return "Boas";
    }

    @Operation(summary = "Crear un nuevo titores")
    @PostMapping("/")
    public Titor crearTutor(@RequestBody Titor titores) {
        return titorService.crearOActualizarTutores(titores);
    }

    @Operation(summary = "Obtener todos los titores")
    @GetMapping("/")
    public List<Titor> obtenerTodosLosTutores() {
        return titorService.obtenerTodosLosTutores();
    }

    @Operation(summary = "Obtener titores por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Titor> obtenerTutorPorId(@PathVariable Long id) {
        Optional<Titor> titor = titorService.obtenerTutorPorId(id);
        return titor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // NUEVO ENDPOINT: Obtener titor con sus alumnos
    @Operation(summary = "Obtener un titor y todos sus alumnos")
    @GetMapping("/{id}/con-alumnos")
    public ResponseEntity<Titor> obtenerTutorConAlumnos(@PathVariable Long id) {
        Optional<Titor> titor= titorService.obtenerTutorConAlumnos(id);
        return titor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un titor")
    @PutMapping("/{id}")
    public ResponseEntity<Titor> actualizarTutor(@PathVariable Long id, @RequestBody Titor titoresDetails) {
        Optional<Titor> titoresOptional = titorService.obtenerTutorPorId(id);
        if (titoresOptional.isPresent()) {
            Titor titor = titoresOptional.get();
            titor.setNome(titoresDetails.getNome());
            titor.setApelidos(titoresDetails.getApelidos()); // Actualizar apellidos también

            Titor titoresActualizado = titorService.crearOActualizarTutores(titor);
            return ResponseEntity.ok(titoresActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un titor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutorPorId(@PathVariable Long id) {
        Optional<Titor> titor = titorService.obtenerTutorPorId(id);
        if (titor.isPresent()) {
            // Verificar si tiene alumnos antes de eliminar
            if (!titor.get().getAlumnos().isEmpty()) {
                // Podría devolver un error 409 Conflict o similar
                return ResponseEntity.badRequest().build();
            }
            titorService.eliminarTutorPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```

/java/org.example/model/Alumno.java
```java
package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_alumno;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "apelidos", nullable = false, length = 150)
    private String apelidos;

    @ManyToOne
    @JoinColumn(name = "id_titor", nullable = false)
    private Titor titor;


    public Alumno(String nome, String apelidos, Titor titor) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.titor = titor;
    }

    public Alumno(){}

    // Getters y setters
    public int getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }

    public Titor getTitor() {
        return titor;
    }

    public void setTitor(Titor titor) {
        this.titor = titor;
    }
}
```

/java/org.example/model/Titor.java
```java
package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titor")
public class Titor{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_titor;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    //relacion
    @OneToMany(mappedBy = "titor",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Alumno> alumnos;

    @Column(name = "apelidos")
    private String apelidos;

    //constructor
    public Titor(String nome, String apelidos) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.alumnos = new ArrayList<>();
    }
    //constructor vacio
    public Titor(){}

    //getters y setters
    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public int getId_titor() {
        return id_titor;
    }

    public void setId_titor(int id_titor) {
        this.id_titor = id_titor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }
    //
    @Override
    public String toString() {
        return "Titores{" +
                "id_titor=" + id_titor +
                ", nome='" + nome + '\'' +
                ", apelidos='" + apelidos + '\'' +
                '}';
    }
}

```

/java/org.example/repository/AlumnoRepository.java
```java
package org.example.repository;

import org.example.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
```

/java/org.example/repository/TitorRepository.java
```java
package org.example.repository;

import org.example.model.Alumno;
import org.example.model.Titor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitorRepository extends JpaRepository<Titor, Long> {
}
```

/java/org.example/service/AlumnoService.java
```java
package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Alumno;
import org.example.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {
    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository){
        this.alumnoRepository = alumnoRepository;
    }
    @Transactional
    public Alumno crearAlumno(Alumno alumno){
        return alumnoRepository.save(alumno);
    }
    public List<Alumno> obterTodoslosAlumnos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> obterPersoaPorId(Long id) { // Cambiado a Long
        return alumnoRepository.findById(id);
    }
}
```

/java/org.example/service/TitorService.java
```java
package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Titor;
import org.example.repository.TitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitorService {

    private final TitorRepository titorRepository;

    @Autowired
    public TitorService(TitorRepository titorRepository) {
        this.titorRepository = titorRepository;
    }

    @Transactional
    public Titor crearOActualizarTutores(Titor titor) {
        return titorRepository.save(titor);
    }

    public List<Titor> obtenerTodosLosTutores() {
        return titorRepository.findAll();
    }


    public Optional<Titor> obtenerTutorPorId(Long id) {
        return titorRepository.findById(id);
    }

    public Optional<Titor> obtenerTutorConAlumnos(Long id) {
        return titorRepository.findById(id);
    }

    public void eliminarTutorPorId(Long id) {
        titorRepository.deleteById(id);
    }
}
```

/java/org.example/Main.java
```java
package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan({"org.example"})
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
}
```

/resources/application.properties
```
app.version=1.0.0

spring.datasource.url=jdbc:postgresql://10.0.9.100:5432/probas
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
server.port=8081
```


/pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.2</version>
        <relativePath/>
    </parent>
    <groupId>org.example</groupId>
    <artifactId>hibernateSwagger</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <!-- PostgreSQL and Hibernate dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.5.4</version> <!-- Make sure to use the latest version -->
        </dependency>

        <!-- Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Test for testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Springdoc OpenAPI for Swagger documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>

        <!-- Spring REST Docs dependencies for documentation -->
        <dependency>
            <groupId>org.springframework.restdocs</groupId>
            <artifactId>spring-restdocs-mockmvc</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```