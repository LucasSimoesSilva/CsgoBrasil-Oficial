package com.sd.csgobrasil.unitario.service;

import com.sd.csgobrasil.entity.Movement;
import com.sd.csgobrasil.repository.MovementRepository;
import com.sd.csgobrasil.service.MovementService;
import com.sd.csgobrasil.service.SkinService;
import com.sd.csgobrasil.service.UserService;
import com.sd.csgobrasil.service.UserSkinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MovementServiceTest {

    @MockBean
    private MovementRepository repository;


    @MockBean
    private UserService userService;


    @MockBean
    private SkinService skinService;


    @MockBean
    private UserSkinService userSkinService;

    @Autowired
    MovementService service;

    @Test
    public void fazerAlgo(){

    }
}
