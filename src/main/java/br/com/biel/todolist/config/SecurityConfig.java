package br.com.biel.todolist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import br.com.biel.todolist.services.UsuarioDetailsService;

@Configuration
public class SecurityConfig {

    //attributes injetados
    @Autowired
    private UsuarioDetailsService uds;

    
    //setando todas as permissoes e configurando nossa "basic authentication"
    @Bean
    public SecurityFilterChain setandoPermissoes(HttpSecurity http) throws Exception{
        //colocando permissoes nas rotas
        http
            .httpBasic() //vamos usar o "autenticacao basica" / uma autenticacao simples
            .and()
            //setando permissoes para cada rota
            .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/home").authenticated()
                .requestMatchers(HttpMethod.GET, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/login-created-success").permitAll()
                .requestMatchers(HttpMethod.GET, "/register").permitAll() //qualquer usuario poderá acessar essa pagina para criar um novo usuário
                .requestMatchers(HttpMethod.POST, "/register").permitAll() //qualquer usuario poderá acessar essa pagina para criar um novo usuário
                .anyRequest().authenticated() //qualquer outra rota, permissao total;
            .and()
            // solicitacoes sensiveis(post,put,delete) poderao ser feitas pelo usuario autenticado.
            .csrf().disable();
            

        //login page
        http
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home") //Acertou o login, aparece essa pagina
                .failureUrl("/login-erro") //Errou o login, aparece essa pagina // vamos replicar a pagina de login, só que agora com uma div com a mensagme de erro.
                .permitAll(); //  depois de logado, permissao total


        //logout page
        http
            .logout()
                .logoutUrl("/sair") // Essa eh a URL que usamos para deslogar
                .logoutSuccessUrl("/login?logout") //essa eh a pagina que vai ser redirecionada quando o usuario deslogar
                .permitAll()
                .invalidateHttpSession(true) //assim que deslogado, invalidamos a porra toda
                .deleteCookies("JSESSIONID"); //removemos os cookies
        return http.build();
    }



    //method para transformar uma String em Hash
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //criando nossas autenticacoes
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(uds).passwordEncoder(new BCryptPasswordEncoder());
    }
}
