package com.sd.csgobrasil.controllers;

import com.sd.csgobrasil.entity.DTO.MovementsId;
import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movement")
public class MovementController {

    @Autowired
    private MovementService service;

    @Autowired
    private SkinService skinService;

    @GetMapping("/movements")
    public ResponseEntity<List<Movement>> listMovement(){
        List<Movement> movements = service.listMovement();
        return ResponseEntity.status(HttpStatus.OK).body(movements);
    }

    @GetMapping("/skinMovements")
    public ResponseEntity<List<SkinMovement>> listMovementSkin(){
        List<Movement> movements = service.listMovement();
        List<SkinMovement> skinMovements = skinService.getSkinMovements(movements);
        return ResponseEntity.status(HttpStatus.OK).body(skinMovements);
    }

    @PostMapping
    public ResponseEntity<Movement> addMovement(@RequestBody Movement m){
        Movement movement = service.addMovement(m);
        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @GetMapping("/{idVenda}")
    public ResponseEntity<Movement> findByMovementId(@PathVariable Long idVenda){
        Movement movement = service.findByMovementId(idVenda);
        return ResponseEntity.status(HttpStatus.OK).body(movement);
    }

    @PutMapping
    public ResponseEntity<Boolean> makeMovement(@RequestBody MovementsId movementsId){
        boolean b = service.makeMovement(movementsId.getIdVenda(), movementsId.getIdComprador());
        return ResponseEntity.status(HttpStatus.OK).body(b);
    }

    @GetMapping("/report")
    public ResponseEntity<List<Report>> makeReport(){
        List<Report> reports = service.makeReport();
        return ResponseEntity.status(HttpStatus.OK).body(reports);
    }

    @DeleteMapping("/{idVenda}")
    public ResponseEntity<Void> cancelMovement(@PathVariable Long idVenda){
        service.cancelMovement(idVenda);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
