package br.com.fiap.cashflowpro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cashflowpro.model.Categoria;




@RequestMapping("/categoria")
@RestController
public class CategoriaController {

    Logger log = LoggerFactory.getLogger(getClass());

    List<Categoria> repository = new ArrayList<>();

    //GET
    @GetMapping()//n precisa mais d: produces = "application/json"
    public List<Categoria> index(){
        return repository;
    }

    //POST
    @PostMapping()
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria){
        // categoria.setId(new Random().nextLong()); //esse codigo é responsabilidade da Categoria, e n deve estar aqui
        log.info("cadastrando categoria: {}", categoria);
        repository.add(categoria);
        return ResponseEntity.status(201).body(categoria);
    }

    //GET
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> get (@PathVariable() Long id) {
        log.info("buscando categoria com id {}", id);

        //stream
        var categoria = getCategoriaById(id); //esses codigo filtra por id e pega o primeiro resultado
        
        if (categoria.isEmpty()){
                return ResponseEntity.notFound().build();
            }
                return ResponseEntity.ok(categoria.get());
    }


    private Optional<Categoria> getCategoriaById(Long id) {
        var categoria = repository
            .stream()
            .filter(c -> c.id().equals(id))
            .findFirst();
        return categoria;
    }

    //Delete
    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id){
        log.info("apagando categoria {}", id);

        var categoria = getCategoriaById(id);
        
        if (categoria.isEmpty()){
                return ResponseEntity.notFound().build();
            }

        repository.remove(categoria.get());

        return ResponseEntity.noContent().build();
    }
    
    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(
        @PathVariable Long id,
        @RequestBody Categoria categoria
    ){
        log.info("atualizado categoria com id {} para {}", id, categoria);

        var categoriaEncontrada = getCategoriaById(id);
        
        if (categoriaEncontrada.isEmpty()){
                return ResponseEntity.notFound().build();
            }

        var categoriaAtualizada = new Categoria(id, categoria.nome(), categoria.icone());
        repository.remove(categoriaEncontrada.get());
        repository.add(categoriaAtualizada);
        
        return ResponseEntity.ok(categoriaAtualizada);
    }
}
