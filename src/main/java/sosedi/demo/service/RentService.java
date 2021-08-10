package sosedi.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sosedi.demo.entity.Objects;
import sosedi.demo.repository.ObjectsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {

    private final ObjectsRepository objectsRepository;

    public List<Objects> findAllObjectsByName(String objectName) {
        List<String> words = Arrays.asList(objectName.split(" "));
        List<Objects> objects = new ArrayList<>();
        words.forEach(word -> objects.addAll(objectsRepository.findAllByNameContains(word.toLowerCase())));
        return objects;
    }
}
