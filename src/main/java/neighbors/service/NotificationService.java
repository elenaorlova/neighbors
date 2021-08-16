package neighbors.service;

import lombok.RequiredArgsConstructor;
import neighbors.entity.Advert;
import neighbors.entity.Notification;
import neighbors.entity.User;
import neighbors.enums.NotificationStatus;
import neighbors.repository.AdvertRepository;
import neighbors.repository.NotificationRepository;
import neighbors.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AdvertRepository advertRepository;

//    @Scheduled(fixedDelay = 3000)
//    public void createAndSaveNotification() {
//        List<Advert> adverts = advertRepository.findAll();
//        List<User> users = userRepository.findAllBySentNotifications(true);
//
//        adverts.forEach( advert -> {
//            users.forEach( user -> {
//                if (user.getDistricts().contains(advert.getDistrict())) {
//
//                }
//            });
//        });
//
//
//        Notification notification = new Notification();
//        notification.setChatId(user.getChatId());
//        notification.setAdvert(advert);
//        notification.setStatus(NotificationStatus.CREATED.name());
//        notificationRepository.save(notification);
//    }
}
