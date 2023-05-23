package br.com.biel.todolist.models;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuario")
public class Usuario {
    
    //attributes
    
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 15, message = "Username must contain between 3 and 20 characters")
    @Column(name = "name", length = 300, nullable = false, unique = true)
    private String name;

    @Email(message = "Input a valid email")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email", length = 300, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, message = "Password must contain at least 3 characters")
    @Column(name = "password", length = 300, nullable = false)
    private String password;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Task> tasks;

    //constrollers
    public Usuario(){}

    public Usuario(String name, String email, String password, List<Task> tasks){
        this.name = name;
        this.email = email;
        this.password = password;
        this.tasks = tasks;
    }

    

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    

    //toString()
    @Override
    public String toString() {
        return "Usuario [name=" + name + ", email=" + email + ", password=" + password + "]";
    }
}
