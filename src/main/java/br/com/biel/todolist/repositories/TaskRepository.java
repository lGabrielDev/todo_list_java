package br.com.biel.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.biel.todolist.models.Task;
import br.com.biel.todolist.models.Usuario;
import jakarta.transaction.Transactional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    //queries


    //Listar todos as tasks, filtrando pelo dono da task
    @Query(value = "SELECT t FROM Task t WHERE t.usuario = :owner ORDER BY id ASC")
    public List<Task> listarTasksDoUsuario(@Param(value = "owner") Usuario donoDaLista);

    
    //DELETE task // por algum motivo a query pronta "deleteById()" nao está funcionando. Resolvi o problema, criando essa query na mão.
    @Modifying
    @Transactional
    @Query(value =  "DELETE FROM Task t WHERE t.id = :id")
    public void deleteTask(@Param(value = "id") Long task_id);

}
