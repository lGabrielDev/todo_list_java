package br.com.biel.todolist.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.biel.todolist.models.Task;
import br.com.biel.todolist.models.Usuario;
import br.com.biel.todolist.repositories.TaskRepository;

@Service
public class TaskService {
    
    //attributes injetados
    @Autowired
    private TaskRepository tr;


    // ----------------------- READ -----------------------
    //All tasks
    public List<Task> listarTodos(Usuario donoDaTask){
        return this.tr.listarTasksDoUsuario(donoDaTask);
    }




    //Search a task by id
    public Optional<Task> procurarTask(Long id){
        Optional<Task> tOptional = this.tr.findById(id);

        return tOptional;
    }



    // ----------------------- CREATE -----------------------
    public void createTask(Task novaTask, Usuario taskOwner){
        novaTask.setUsuario(taskOwner); // colocamos um usuario para essa task
        this.tr.save(novaTask);
    }



    // ----------------------- UPDATE -----------------------
    public void modifyTask(Task taskModificada){
        this.tr.save(taskModificada);
    }



    // ----------------------- DELETE -----------------------
    public void deleteTask(Long task_id, Usuario usuarioLogado){ 
        //verificamos se essa task existe no banco.
        Optional<Task> tOptional = this.procurarTask(task_id);

        if(tOptional.isPresent()){
            Task taskParaDeletar = tOptional.get();
            //só vamos deletar se o "id do usuario autenticado" for o mesmo do "id do dono da task"
            if(usuarioLogado.getId() == taskParaDeletar.getUsuario().getId()){
                this.tr.deleteTask(task_id); // deletamos a task do banco. Lembrando que o "deleteById()" nao está funcionando
            }
        }
    }
}
