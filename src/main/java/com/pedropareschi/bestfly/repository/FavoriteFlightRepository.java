package com.pedropareschi.bestfly.repository;

import com.pedropareschi.bestfly.entity.FavoriteFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteFlightRepository extends JpaRepository<FavoriteFlight, Long> {
    List<FavoriteFlight> findByUserId(Long userId);
}
