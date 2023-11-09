package com.sd.csgobrasil.repository;

import com.sd.csgobrasil.entity.DTO.SkinWithState;
import com.sd.csgobrasil.entity.DTO.UserSkin;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserSkinRepository {

    private PreparedStatement st;
    private ResultSet rs;

    /*public List<SkinWithState> listSkinsWithStateFromUser(Long idUser) {
        String mySql = """
                        SELECT *
                            FROM (
                                SELECT s.id AS id_skin, s.nome, s.arma, s.preco, s.raridade, s.imagem, estado_venda,
                                CASE
                                    WHEN EXISTS (SELECT 1 FROM movement WHERE id_skin = s.id) THEN\s
                                        CASE
                                            WHEN (SELECT estado_venda FROM movement WHERE id_skin = s.id AND estado_venda = false) = false
                                            THEN true
                                            ELSE false
                                        END\s
                                    ELSE false\s
                                END AS is_in_movement,
                                CASE\s
                                    WHEN EXISTS (SELECT 1 FROM movement WHERE id_skin = s.id) THEN\s
                                        CASE\s
                                            WHEN (SELECT estado_venda FROM movement WHERE id_skin = s.id AND estado_venda = false) = false
                                            THEN m.id_venda
                                            ELSE 0\s
                                        END\s
                                    ELSE 0   \s
                                END AS id_venda
                                FROM user_skins_user su
                                LEFT JOIN skin s ON s.id = su.skins_user_id\s
                                LEFT JOIN movement m ON s.id = m.id_skin\s
                                WHERE su.user_id = ?
                            ) AS subquery
                            WHERE estado_venda = false OR (estado_venda = true AND is_in_movement = false);
                            
                    """;
        List<SkinWithState> skins = new ArrayList<>();

        try (Connection conn = ConnectionJdbc.getConnection()) {
            st = conn.prepareStatement(mySql);
            st.setLong(1, idUser);
            rs = st.executeQuery();
            List<Long> idsList = new ArrayList<>();
            while (rs.next()) {
                SkinWithState s = new SkinWithState();
                s.setIdSkin(rs.getLong("id_skin"));
                s.setNome(rs.getString("nome"));
                s.setArma(rs.getString("arma"));
                s.setPreco(rs.getInt("preco"));
                s.setRaridade(rs.getString("raridade"));
                s.setImagem(rs.getString("imagem"));
                s.setInMovement(rs.getBoolean("is_in_movement"));
                s.setIdVenda(rs.getLong("id_venda"));

                if(!idsList.contains(s.getIdSkin())){
                    idsList.add(s.getIdSkin());
                    skins.add(s);
                }

            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar skins do usuário\n");
        }

        return skins;
    }*/
}
