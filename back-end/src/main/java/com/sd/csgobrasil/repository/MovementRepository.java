package com.sd.csgobrasil.repository;

import com.sd.csgobrasil.entity.DTO.Report;
import com.sd.csgobrasil.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(value =
            "SELECT m.id_venda as idVenda, uc.nome AS nomeComprador, uv.nome AS nomeVendedor, "+
            "CONCAT(s.arma, ' ', s.nome) AS nomeSkin, m.pontos, m.estado_venda as estadoVenda "+
                            "FROM movement AS m "+
                            "LEFT JOIN user AS uc ON m.id_comprador = uc.id "+
                            "LEFT JOIN user AS uv ON m.id_vendedor = uv.id "+
                            "LEFT JOIN skin AS s ON m.id_skin = s.id "+
                            "ORDER BY m.id_venda"
            , nativeQuery = true)
    List<Report> makeReport();

}
