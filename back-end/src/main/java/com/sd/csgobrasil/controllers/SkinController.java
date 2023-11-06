package com.sd.csgobrasil.controllers;

import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.SkinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skin")
public class SkinController {


    @Autowired
    private SkinService service;

    @GetMapping("/skins")
    public ResponseEntity<List<Skin>> listSkins(){
        List<Skin> skins = service.listSkins();

        return ResponseEntity.status(HttpStatus.OK).body(skins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skin> findBySkinId(@PathVariable Long id){
        Skin skin = service.findBySkinId(id);
        return ResponseEntity.status(HttpStatus.OK).body(skin);
    }

    @PostMapping
    public ResponseEntity<Skin> saveSkin(@RequestBody Skin skin){
        Skin addSkin = service.addSkin(skin);
        return ResponseEntity.status(HttpStatus.CREATED).body(addSkin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skin> updateSkin(@PathVariable Long id, @RequestBody Skin skin){
        Skin skinUpdated = service.updateSkin(id, skin);
        return ResponseEntity.status(HttpStatus.OK).body(skinUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkin(@PathVariable Long id){
        service.deleteSkin(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
