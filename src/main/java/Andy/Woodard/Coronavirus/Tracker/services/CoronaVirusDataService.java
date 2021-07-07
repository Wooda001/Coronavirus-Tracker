package Andy.Woodard.Coronavirus.Tracker.services;

import Andy.Woodard.Coronavirus.Tracker.models.LocationStats;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.commons.csv.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CoronaVirusDataService {
    //TODO: NEED TO CHANGE THIS TO REFLECT CURRENT DATE/TIME RATHER THAN STATIC
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/07-04-2021.csv";



    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            if (!Objects.equals(record.get("Country_Region"), "US")) {
                LocationStats locationStat = new LocationStats();
                locationStat.setState(record.get("Province_State"));
                locationStat.setCountry(record.get("Country_Region"));
                //String totalDeaths = record.get("Deaths");
                locationStat.setLatestTotalCases(record.get("Active"));
                newStats.add(locationStat);
            }

        }
        this.allStats = newStats;
    }
    public int calculateTotal(){
        int total = 0;
        for (LocationStats stat : allStats){
            try{
                total += Integer.parseInt(stat.getLatestTotalCases());
            } catch (Exception e){
                total += 0;
            }
        }
        return total;
    }

}
 