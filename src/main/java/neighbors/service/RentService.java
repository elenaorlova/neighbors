package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.repository.AdvertRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {

    private final AdvertRepository advertRepository;

    public List<Advert> findAllObjectsByName(String objectName) {
        List<String> words = Arrays.asList(objectName.split(" "));
        List<Advert> objects = new ArrayList<>();
        words.forEach(word -> objects.addAll(advertRepository.findAllByNameContains(word.toLowerCase())));
        return objects;
    }
}
