package ebnatural.bizcurator.apiserver.service;

import ebnatural.bizcurator.apiserver.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProductClicksResetService {
    private ProductRepository productRepository;

    // 매주 월요일 00:00에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetWeeklyClicks() {
        productRepository.resetWeeklyClicks();
    }

    // 매월 1일 00:00에 실행
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyClicks() {
        productRepository.resetMonthlyClicks();
    }
}