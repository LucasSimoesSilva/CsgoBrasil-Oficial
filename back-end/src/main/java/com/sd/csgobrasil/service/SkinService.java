package com.sd.csgobrasil.service;

import com.sd.csgobrasil.entity.DTO.SkinMovement;
import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.entity.Skin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sd.csgobrasil.repository.SkinRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SkinService {

    @Autowired
    private SkinRepository repository;

    public List<Skin> listSkins() {
        return repository.findAll();
    }

    public Skin addSkin(Skin skin) {
        return repository.save(skin);
    }

    public Skin updateSkin(Long id, Skin skin) {
        skin.setId(id);
        return repository.save(skin);
    }

    public Skin findBySkinId(Long id) {
        return repository.findById(id).get();
    }

    public List<SkinMovement> getSkinMovements(List<Movement> movements){
        List<SkinMovement> movementsList = new ArrayList<>();
        for (Movement movement : movements) {
            if(!movement.isEstadoVenda() || movement.getIdVendedor() == 4){
                Skin skin = findBySkinId(movement.getIdSkin());
                SkinMovement skinMovement = new SkinMovement(movement.getIdVenda(),movement.getIdVendedor()
                        ,movement.isEstadoVenda(),skin.getNome(),skin.getArma(), skin.getPreco(),skin.getRaridade()
                ,skin.getImagem());
                movementsList.add(skinMovement);
            }
        }
        return movementsList;
    }

    public void deleteSkin(Long id){
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
        }else {
            throw new NoSuchElementException();
        }
    }
}
