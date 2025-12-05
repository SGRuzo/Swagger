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