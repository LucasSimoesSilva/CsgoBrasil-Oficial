package com.sd.csgobrasil.service;

import com.sd.csgobrasil.entity.DTO.*;
import com.sd.csgobrasil.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sd.csgobrasil.repository.UserRepository;

import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepository repository;


    @Autowired
    private UserSkinService userSkinService;

    public List<User> listUsers() {
        List<User> users = repository.findAll();
        return fillSkins(users);
    }

    private List<User> fillSkins(List<User> users){
        for (User user : users) {
            user.setSkinsUser(userSkinService.listSkinsFromUser(user.getId()));
        }
        return users;
    }

    public boolean checkIfUserExist(UserRegister userRegister) {
        return repository.existsUserByEmailOrNome(userRegister.getEmail(), userRegister.getNome());
    }

    public boolean checkLoginUser(UserLogin userLogin){return repository.existsUserByEmailAndSenha(userLogin.getEmail(), userLogin.getSenha());}

    public User getUserInfo(String email){
        return repository.findUsersByEmail(email);
    }

    public User addUser(User user){
        user.setCargo("cliente");
        user.setPontos(1000);
        return repository.save(user);
    }

    public User updateUser(Long id, User user){
        user.setId(id);
        return repository.save(user);}

    public User findByUserId(Long id){return repository.findById(id).get();}

    public void deleteUser(Long id){repository.deleteById(id);}

}
