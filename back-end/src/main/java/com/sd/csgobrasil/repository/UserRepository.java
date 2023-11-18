package com.sd.csgobrasil.repository;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import com.sd.csgobrasil.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByEmailOrNome(String email, String nome);

    Boolean existsUserByEmailAndSenha(String email, String senha);

    User findUsersByEmail(String email);

    @Query(value = "SELECT skins_user_id as idSkin, user_id as idUser FROM usuario_skins_user WHERE user_id=?",nativeQuery = true)
    List<UserSkin> listSkinsFromUser(Long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from usuario_skins_user where skins_user_id = ? and user_id= ?",nativeQuery = true)
    void deleteSkinFromUser(Long skinsPossuidasId, Long userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT into usuario_skins_user(skins_user_id,user_id) values(?,?)",nativeQuery = true)
    void addSkinFromUser(Long skinsPossuidasId, Long userId);

    @Query(value = """
                        SELECT *
                            FROM (
                                SELECT s.id AS idSkin, s.nome, s.arma, s.preco, s.raridade, s.imagem, estado_venda as estadoVenda,
                                CASE
                                    WHEN EXISTS (SELECT 1 FROM movement WHERE id_skin = s.id) THEN
                                        CASE
                                            WHEN (SELECT estado_venda FROM movement WHERE id_skin = s.id AND estado_venda = false) = false
                                            THEN true
                                            ELSE false
                                        END
                                    ELSE false
                                END AS isInMovement,
                                CASE
                                    WHEN EXISTS (SELECT 1 FROM movement WHERE id_skin = s.id) THEN
                                        CASE
                                            WHEN (SELECT estado_venda FROM movement WHERE id_skin = s.id AND estado_venda = false) = false
                                            THEN m.id_venda
                                            ELSE 0
                                        END
                                    ELSE 0
                                END AS idVenda
                                FROM usuario_skins_user su
                                LEFT JOIN skin s ON s.id = su.skins_user_id
                                LEFT JOIN movement m ON s.id = m.id_skin
                                WHERE su.user_id = ?
                            ) AS subquery
                            WHERE estadoVenda = false OR (estadoVenda = true AND isInMovement = false);
                    """, nativeQuery = true)
    List<SkinWithState> listSkinsWithStateFromUser(Long idUser);

}
