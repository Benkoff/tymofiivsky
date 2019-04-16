package io.github.benkoff.tymofiivsky;

import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import io.github.benkoff.tymofiivsky.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class H2Bootstrap implements CommandLineRunner {
    @Autowired RoomRepository roomRepository;

    @Override
    public void run(String... args) {
        roomRepository.save(new RoomEntity("101", 60));
        roomRepository.save(new RoomEntity("102", 60));
        roomRepository.save(new RoomEntity("201", 30));
        roomRepository.save(new RoomEntity("202", 30));
        roomRepository.save(new RoomEntity("203", 30));
        roomRepository.save(new RoomEntity("204", 60));
        roomRepository.save(new RoomEntity("301", 30));

        System.out.println("Bootstrapped data:");
        roomRepository.findAll().forEach(System.out::println);
    }
}
