package br.com.biel.todolist.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import br.com.biel.todolist.models.Usuario;
import br.com.biel.todolist.services.UsuarioService;
import jakarta.validation.Valid;

@Controller
public class LoginAndRegisterController {
    
    //attributes injetados
    @Autowired
    private UsuarioService us;



    // -------------------------------------------------- LOGIN PAGE --------------------------------------------------
    // --------------------- Default Login Page ---------------------
    @GetMapping("/login")
    public ModelAndView loginPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("./login.html");
        return mv;
    }



    // --------------------- Login Page  + "Success message" - When is created a new user ---------------------
    @GetMapping("/login-created-success")
    public ModelAndView loginPageCadastradoComSucesso(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("./login.html");

        Boolean cadastradoComSucesso = true;
        String mensagemSucesso = "Registration completed successfully!";

        mv.addObject("cadastradoComSucesso", cadastradoComSucesso);
        mv.addObject("mensagemSucesso", mensagemSucesso);

        return mv;
    }



    // --------------------- Login Page  + "Error Message" -> When the username or password is invalid ---------------------
    @GetMapping("/login-erro")
    public ModelAndView loginPageError(){
        String mensagemErroLogin = "Login or password invalid";

        ModelAndView mv = new ModelAndView();
        mv.setViewName("./login.html");
        mv.addObject("mensagemErroLogin", mensagemErroLogin);

        return mv;
    }



   // --------------------- POST /login ---------------------
    //Não precisamos criar essa action, pois o spring security já cria pra nós.
    //Automaticamente o spring security já cria uma action POST para verificar se o usuario logado está correto. No formulário, basta voce especificar:

    //method = POST
    //action = /login
    //input name = username --> campo de login
    //input name = password --> campo de password




    // -------------------------------------------------- REGISTER PAGE --------------------------------------------------
    // --------------------- GET ---------------------
    @GetMapping("/register")
    public ModelAndView registerPage(){
        //objeto "Usuario" para o template/html ter acesso. Já que nosso formulario retorna um objeto "Usuario", precisar informar pra ele.
        Usuario u1 = new Usuario();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("./register.html");
        mv.addObject("usuario", u1);
        return mv;
    }


// --------------------- POST ---------------------
    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid Usuario novoUsuario, BindingResult br){
        //retornamos a mesma view/página. Só que agora, mostramos as mensagens de erro.
        ModelAndView mv = new ModelAndView();
        mv.setViewName("./register.html");

        //mensagens de erro - NAME
        String nameErro1 = "Username must contain between 3 and 20 characters";
        String nameErro2 = "Username cannot be null";
        String nameErro3 = "Username already exists!";

        //mensagens de erro - EMAIL
        String emailErro1 = "Email cannot be null";
        String emailErro2 = "Email already exists!";

        //mensagens de erro - PASSWORD
        String passwordErro1 = "Password must contain at least 3 characters";

        //se o usuário desrespeitar algum campo, mostramos o campo que ele errou.
        if(br.hasErrors()){
            //ERROS DO CAMPO "NAME"
            if(novoUsuario.getName().length() > 0 && novoUsuario.getName().length() < 3 || novoUsuario.getName().length() > 20){  //name --> entre 1 e 2 OU maior que 20
                mv.addObject("nameErro1", nameErro1); 
            }

            if(novoUsuario.getName().length() == 0){ //nao digitou nada
                mv.addObject("nameErro2", nameErro2);
            }
            
            //ERROS DO CAMPO "EMAIL"
            if(novoUsuario.getEmail().length() == 0){ //email em branco
                mv.addObject("emailErro1", emailErro1);
            }

            //ERROS DO CAMPO "PASSWORD"
            if(novoUsuario.getPassword().length() < 3){ //senha deve ter pelo menos 3 characters.
                mv.addObject("passwordErro1", passwordErro1);
            }
            
            return mv; //retornamos a mesma pagina com todos os erros.
        }

        //se tudo tiver ok e nao tiver erro, precisamos verificar se o "usuario" ou o "email" já existem no banco. Como a UNIQUE constraint não está funcionando, fazemos assim:
        
        //Booleanos
        Boolean usernameJaExiste = false;
        Boolean emailJaExiste = false;


        //verificamos se o "username" ja existe
        Optional<Usuario> uOptional1 = this.us.procurarUsuarioPeloName(novoUsuario.getName());
        if(uOptional1.isPresent()){
            usernameJaExiste = true;
        }

        //verificamos se o "email" ja existe
        Optional<Usuario> uOptional2 = this.us.procurarUsuarioPeloEmail(novoUsuario.getEmail());
        if(uOptional2.isPresent()){
            emailJaExiste = true;
        }


        //se o "username" ou "email" ja existir no sistema, mostramos a mensagem de erro.
        if(usernameJaExiste && emailJaExiste){
            mv.addObject("nameErro3", nameErro3);
            mv.addObject("emailErro2", emailErro2);
            return mv;
        }
        else if(usernameJaExiste){
            mv.addObject("nameErro3", nameErro3);
            return mv;
        }
        else if(emailJaExiste){
            mv.addObject("emailErro2", emailErro2);
            return mv;
        }

        //se tiver tudo OK, cadastramos
        this.us.createUser(novoUsuario);
        mv.setViewName("redirect:/login-created-success"); //redirecionamos para a action com a mensagem de sucesso
        return mv;
    }
}
