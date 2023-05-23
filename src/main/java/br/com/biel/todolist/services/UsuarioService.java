package br.com.biel.todolist.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.biel.todolist.models.Usuario;
import br.com.biel.todolist.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    //attributes injetados
    @Autowired
    private UsuarioRepository ur;

    // ----------------------- READ -----------------------

    ////Search a user by id
    public Optional<Usuario> procurarUsuario(Long id){
        Optional<Usuario> uOptional = this.ur.findById(id);
        return uOptional;

    }

    //Search a user by "username";
    public Optional<Usuario> procurarUsuarioPeloName(String username){
        Optional<Usuario> uOptional = this.ur.findByName(username);
        return uOptional;
    }


    //Search a user by "email";
    public Optional<Usuario> procurarUsuarioPeloEmail(String email){
        Optional<Usuario> uOptional = this.ur.findByEmail(email);
        return uOptional;
    }


// ----------------------- CREATE -----------------------
    public void createUser(Usuario newUser){
        BCryptPasswordEncoder objetoEncouder = new BCryptPasswordEncoder(); //criamos um objeto da class "BCryptPasswordEncoder". Nessa class, existe um method "encode" que vamos usar para transformar a senha do usuairo emm um Hash.
        //usamos o method "encode" e transformamos a senha em Hash.
        String NovaSenha = objetoEncouder.encode(newUser.getPassword());

        //setamos uma nova senha para o usuario. Aquele ao inves de "123", a senha dele é "sad65#@#@a4d46s{}66546ad46dsa6a". Sacou?
        newUser.setPassword(NovaSenha);

        //cadastramos esse novo Usuario no banco
        this.ur.save(newUser);
    }
}
