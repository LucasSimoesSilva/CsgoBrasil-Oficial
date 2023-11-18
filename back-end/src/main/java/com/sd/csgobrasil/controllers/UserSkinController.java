package com.sd.csgobrasil.controllers;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.Skin;
import com.sd.csgobrasil.service.UserSkinService;
import com.sd.csgobrasil.util.SkinWithStateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/userskin")
public class UserSkinController {


    @Autowired
    UserSkinService service;

    @GetMapping("/skinsState/{idUser}")
    public ResponseEntity<List<SkinWithState>> listSkinsWithState(@PathVariable Long idUser){
        List<SkinWithState> skinsUser = service.listSkinsWithStateFromUser(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(skinsUser);
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<List<Skin>> listSkinsFromUser(@PathVariable Long idUser){
        List<Skin> skinsUser = service.listSkinsFromUser(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(skinsUser);
    }
}
