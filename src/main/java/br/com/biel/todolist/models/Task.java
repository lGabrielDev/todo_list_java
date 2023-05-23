package br.com.biel.todolist.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_tasks")
public class Task {
    
    //attributes
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 300)
    @NotBlank(message = "Task name cannot be blank")
    @Size(max = 36, message = "Task name must contain up to 40 characters")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    //constructors
    public Task(){}

    public Task(String name, Usuario usuario){
        this.name = name;
        this.usuario = usuario;
    }



    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    

    //toString()
    @Override
    public String toString() {
        return String.format(("Task_id: %s\tTask_name: %s\tTask owner: %s"),this.id, this.name, this.usuario.getName());
    }
}
