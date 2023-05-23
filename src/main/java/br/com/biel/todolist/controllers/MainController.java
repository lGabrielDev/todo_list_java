package br.com.biel.todolist.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import br.com.biel.todolist.models.Task;
import br.com.biel.todolist.models.Usuario;
import br.com.biel.todolist.models.UsuarioDetails;
import br.com.biel.todolist.services.TaskService;
import br.com.biel.todolist.services.UsuarioService;
import jakarta.validation.Valid;

@Controller
public class MainController{

    //attributes injetados
    @Autowired
    private UsuarioService us;

    @Autowired
    private TaskService ts;




    // ------------------------------------------------------------ CREATE ------------------------------------------------------------
    @GetMapping("/home")
    public ModelAndView homePage(Authentication usuarioAutenticado){

        //vamos transformar o usuario autenticado em um "Usuario"
        UsuarioDetails usuarioLogado = (UsuarioDetails) usuarioAutenticado.getPrincipal(); //criamos um "UsuarioDetails" como "getPrincipal()"
        Usuario usuarioCru = usuarioLogado.getUsuario(); //criamos um usuario com base na autenticacao. Agora sim, conseguimos pegar todas as informacoes do usuario logado.
        Optional<Usuario> uOptional = this.us.procurarUsuarioPeloName(usuarioCru.getName());

        if(uOptional.isPresent()){
            Task tarefa = new Task(); //objeto "Task' para conseguirmos usar no input. O formulario retorna um objeto "Task". Pra isso, precisamos enviar esse objeto para o html conseguir reconhecer.
            List<Task> tarefas = this.ts.listarTodos(usuarioCru); // lista de tarefas do usuario logado.

            ModelAndView mv = new ModelAndView();
            mv.setViewName("./home.html");
            mv.addObject("tarefa", tarefa); //objeto "Task" para o html entender.
            mv.addObject("usuarioLogado", usuarioCru); //agora o html consegue pegar todas as informacoes do usuario logado
            mv.addObject("lista_tarefas", tarefas); //todas as tarefas do usuario logado
            return mv;  
        }
        //else
        ModelAndView mv = new ModelAndView();
        mv.setViewName("./usuario_nao_encontrado.html");
        return mv;  
    }




    @PostMapping("/receberTask")
    public ModelAndView receberTask(@Valid Task novaTask, BindingResult br, Authentication usuarioAutenticado){

        //vamos transformar o usuario autenticado em um "Usuario"
        UsuarioDetails usuarioLogado = (UsuarioDetails) usuarioAutenticado.getPrincipal(); //criamos um "UsuarioDetails" como "getPrincipal()"
        Usuario usuarioCru = usuarioLogado.getUsuario(); //criamos um usuario com base na autenticacao. Agora sim, conseguimos pegar todas as informacoes do usuario logado.
        ModelAndView mv = new ModelAndView();

        //Se nao tiver erro / Se task nao tiver em branco
        if(!(br.hasErrors())){
            this.ts.createTask(novaTask, usuarioCru); // cadastramos a task no banco
            mv.setViewName("redirect:/home");
            return mv;
        }
        //se tiver erro:
        else{
            //possíveis erros de input. Vamos pegar a "mensagem default" que setamos lá na "Task" entity
            FieldError possiveisErros = br.getFieldError("name");
            String temErro1 = possiveisErros.getDefaultMessage(); //Assim, eu ja consigo pegar todos os erros do campo "Name"            
        
            //adicionamos a mensagem de erro para a view
            mv.addObject("temErro1", temErro1);
            mv.setViewName("redirect:/home");
            return mv;
        }
        
    }


// ------------------------------------------------------------ UPDATE ------------------------------------------------------------
    @GetMapping("/task/{id}")
    public ModelAndView editTaskPage(Authentication usuarioAutenticado, @PathVariable(name = "id") Long id){
        ModelAndView mv = new ModelAndView();

        //vamos transformar o usuario autenticado em um "Usuario"
        UsuarioDetails usuarioLogado = (UsuarioDetails) usuarioAutenticado.getPrincipal(); //criamos um "UsuarioDetails" como "getPrincipal()"
        Usuario usuarioCru = usuarioLogado.getUsuario(); //criamos um usuario com base na autenticacao. Agora sim, conseguimos pegar todas as informacoes do usuario logado.
        Optional<Usuario> uOptional = this.us.procurarUsuarioPeloName(usuarioCru.getName());
 

        if(uOptional.isPresent()){
            //antes de mostrar a pagina para editar a task, precisamos saber se o usuario logado realmente é dono daquela task.
            Usuario usuario = uOptional.get();
            //verificamos se a task existe
            Optional<Task> tOptional = this.ts.procurarTask(id);

            //se a task existir
            if(tOptional.isPresent()){
                Task taskParaEditar = tOptional.get();

                //verificamos se usuario logado é o dono da task. Se o "id do usuario logado" for igual ao "id do dono da task", safe.
                if(taskParaEditar.getUsuario().getId() == usuario.getId()){
                    mv.setViewName("./edit_task.html"); // essa página é igualzinha a página "home.html". A diferenca é que ela contem uma div com o "modal" aberto.
                    Task tarefa = new Task(); //objeto "Task' para conseguirmos usar no input. O formulario retorna um objeto "Task". Pra isso, precisamos enviar esse objeto para o html conseguir reconhecer.
                    List<Task> tarefas = this.ts.listarTodos(usuarioCru); // lista de tarefas do usuario logado.
            
                    mv.addObject("tarefa", tarefa); //objeto "Task" para o html entender.
                    mv.addObject("usuarioLogado", usuarioCru); //agora o html consegue pegar todas as informacoes do usuario logado
                    mv.addObject("lista_tarefas", tarefas); //todas as tarefas do usuario logado
                    mv.addObject("taskParaEditar", taskParaEditar); //agora o html tem acesso a essa task
                    return mv;
                }
                //se o usuario logado nao for o dono da task, retornamos para a home
                else{
                    mv.setViewName("redirect:/home");
                    return mv;
                }
            }
            //se a task nao existir, retornamos pra home
            mv.setViewName("redirect:/home");
            return mv;
        }
        //se o usuario nao existir, pagina de erro - "user not found"
        mv.setViewName("./usuario_nao_encontrado.html");
        return mv;  

    }





    @PostMapping("/task/{id}/edit")
    public ModelAndView alterarTask(@PathVariable(name = "id") Long task_id, @Valid Task taskAlterada, BindingResult br){
        
        ModelAndView mv = new ModelAndView();

        //criamos uma "Task" antiga. Vamos pegar o "id" e o "owner_id" antigos e colocar na novaTask. Assim, garantimos que vamos ALTERAR um registro e não criar outro.
        Optional<Task> tOptional = this.ts.procurarTask(task_id);

        if(tOptional.isPresent()){
            //task antiga
            Task oldTask = tOptional.get();
            //nova task
            taskAlterada.setId(oldTask.getId()); //Essa nova task vai receber o mesmo id da antiga. Assim, alteramos e não criamos uma nova task.
            taskAlterada.setUsuario(oldTask.getUsuario()); // Nova task recebe o mesmo dono da task antiga.


            //se tiver erro de input, mostramos a mesma página/modal, mas agora com as mensagens de erro.
            if(br.hasErrors()){
                FieldError erroEncontrado = br.getFieldError("name");
                String mensagemErro = erroEncontrado.getDefaultMessage(); // mensagem de erro que setamos lá na entity "Task"

                mv.addObject("mensagemErro", mensagemErro); //adicionamos a mensagem de erro para a página HTML ter acesso
                mv.setViewName("redirect:/task/{id}"); //quando estamos redirecionando, só podemos adicionar objetos "Simples"/ 1 attribute simples. Quando exibimos a mesma página, podemos enviar um attribute complexo. 
                return mv;
            }
            //Se nao tiver erro, alteremos a task e salvamos no banco. Porém, precisamos veri
            this.ts.modifyTask(taskAlterada);
            mv.setViewName("redirect:/home");
            return mv;
        }
        //se passarmos um "id" de uma task que nao existe, retornamos uma pagina de erro
        mv.setViewName("redirect:/home");
        return mv;  
    }



    
    // ------------------------------------------------------------ DELETE ------------------------------------------------------------
    @GetMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable(name = "id") Long id, Authentication usuarioLogado){
        //tranformando uma "authentication/usuario logado" em um "Usuario"
        UsuarioDetails usuarioDetails = (UsuarioDetails) usuarioLogado.getPrincipal();
        Usuario usuario = usuarioDetails.getUsuario();

        ModelAndView mv = new ModelAndView();
        //deletamos a task do banco. Lembrando, que essa task só sera deletada se o usuario logado for DONO dessa task. Se o "id do usuario logado", bater com o "id do dono da task", deletamos.
        this.ts.deleteTask(id, usuario);
        mv.setViewName("redirect:/home");
        return mv;
    }
}
