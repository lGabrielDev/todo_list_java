package br.com.biel.todolist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.biel.todolist.models.Usuario;


@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{
    //queries
    public Optional<Usuario> findByName(String username);

    public Optional<Usuario> findByEmail(String email);
}
