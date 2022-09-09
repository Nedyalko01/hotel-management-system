package com.example.moonlighthotel.repositories;

import com.example.moonlighthotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room , Long> {

    Optional<Room> findByTitle(String title);
}
