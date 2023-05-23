package br.com.biel.todolist.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.biel.todolist.models.Usuario;
import br.com.biel.todolist.models.UsuarioDetails;
import br.com.biel.todolist.repositories.UsuarioRepository;


@Service
public class UsuarioDetailsService implements UserDetailsService {
    
    //attribute injetado
    @Autowired
    private UsuarioRepository ur;


    //method implementado da interface "UserDetailsService". É com esse method que vamos fazer a autenticacao das credenciais enviadas, "username" and "password". Se tiverem certinho, igual ao banco, autenticacao correta.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Usuario> uOptional = this.ur.findByName(username);

        if(uOptional.isPresent()){
            Usuario u1 = uOptional.get();
            UsuarioDetails ud1 = new UsuarioDetails();
            ud1.setUsuario(u1);
            return ud1;
        }
        //se não encontrar o usuário, enviamos uma mensagem de erro
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
