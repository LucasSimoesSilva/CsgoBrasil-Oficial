package com.sd.csgobrasil.repository;

import com.sd.csgobrasil.entity.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

}
