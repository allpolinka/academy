/*package academy.tochkavhoda.competition.service;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.model.Application;
import academy.tochkavhoda.competition.server.ServerResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SummaryService {
    private final ApplicationDao applicationDao;
    private final Gson gson = new Gson();
    private double totalFund; // Общий фонд, устанавливается в конструкторе или методе
    private double thresholdRating = 0; // Порог, предполагаем 0, как не указано

    public SummaryService(ApplicationDao applicationDao, double totalFund) {
        this.applicationDao = applicationDao;
        this.totalFund = totalFund;
    }

    // Подведение итогов. Логика: Сортируем по алгоритму ТЗ.
    public ServerResponse summarize() {
        List<Application> apps = applicationDao.getAll();
        // Сортировка: по средней оценке desc, затем по сумме asc, затем random
        Collections.shuffle(apps); // Сначала shuffle для random при равенстве
        apps.sort(Comparator.comparingDouble(Application::getRequestedAmount)
                .thenComparing(Comparator.comparingDouble(Application::getAverageRating).reversed()));

        List<Application> granted = new ArrayList<>();
        double remaining = totalFund;
        for (Application app : apps) {
            if (app.getAverageRating() <= thresholdRating) break; // Порог
            if (app.getRequestedAmount() <= remaining) {
                granted.add(app);
                remaining -= app.getRequestedAmount();
            }
        }
        // Возвращаем список грантов в JSON
        return new ServerResponse(200, gson.toJson(granted.stream().map(ApplicationResponse::new).collect(Collectors.toList())));
    }
}*/
