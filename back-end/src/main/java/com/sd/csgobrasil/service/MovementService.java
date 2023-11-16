package com.sd.csgobrasil.service;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sd.csgobrasil.repository.MovementRepository;

import java.util.List;
import java.util.Objects;

@Service
public class MovementService {


    @Autowired
    private MovementRepository repository;


    @Autowired
    private UserService userService;


    @Autowired
    private SkinService skinService;


    @Autowired
    private UserSkinService userSkinService;

    public List<Movement> listMovement(){return repository.findAll();}

    public Movement updateM(Movement m){return repository.save(m);}

    public Movement addMovement(Movement m){return repository.save(m);}

    public Movement findByMovementId(Long id){return repository.findById(id).get();}

    public boolean makeMovement(Long idVenda, Long idComprador){
        Movement movement = findByMovementId(idVenda);
        Skin skinMovement = skinService.findBySkinId(movement.getIdSkin());
        User comprador = userService.findByUserId(idComprador);
        User vendedor = userService.findByUserId(movement.getIdVendedor());

        if (Objects.equals(vendedor.getId(), comprador.getId())) {
            System.out.println("O vendedor não pode comprar sua própria skin");
            return false;
        } else if (comprador.getPontos() >= movement.getPontos()) {
            movement.setEstadoVenda(true);
            movement.setIdComprador(comprador.getId());

            comprador.setPontos(comprador.getPontos() - movement.getPontos());
            vendedor.setPontos(vendedor.getPontos() + movement.getPontos());

            userSkinService.deleteSkinFromUser(skinMovement.getId(), vendedor.getId());
            userSkinService.addSkinFromUser(skinMovement.getId(), comprador.getId());
            userService.updateUser(comprador.getId(), comprador);
            userService.updateUser(vendedor.getId(), vendedor);
            updateM(movement);
            return true;
        }
        return false;
    }
    public List<Report> makeReport(){return repository.makeReport();}

    public void cancelMovement(Long idVenda){
        repository.deleteById(idVenda);
    }
}
