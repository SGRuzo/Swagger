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
