package br.com.biel.todolist.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioDetails implements UserDetails {

    //attribute que vamos usar. Essa autenticacao vai ter o mesmo "username" and "password" do objeto abaixo:
    private Usuario usuario;


    //constructors
    public UsuarioDetails(){}

    public UsuarioDetails(Usuario usuario){
        this.usuario = usuario;
    }



    //getters and setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    


    //methods implementados da interface "UserDetails"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }



    //setamos o "password" and "username" do nosso usuário.
    @Override
    public String getPassword() {
        return this.usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return this.usuario.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
