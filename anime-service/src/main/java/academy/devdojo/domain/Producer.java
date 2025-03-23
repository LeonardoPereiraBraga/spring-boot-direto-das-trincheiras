package academy.devdojo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Producer {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private static List<Producer> producers = new ArrayList<>();

    static {
        var ninjaKamui = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var kaijuu = Producer.builder().id(2L).name("Kyoto").createdAt(LocalDateTime.now()).build();
        var kimetsuNoYaiba = Producer.builder().id(3L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(ninjaKamui, kaijuu, kimetsuNoYaiba));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}
