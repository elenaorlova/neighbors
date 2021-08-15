package sosedi.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sosedi.demo.entity.Advert;
import sosedi.demo.repository.AdvertRepository;

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
