package com.covidseries.covid19_Interactivemap.services;

import com.covidseries.covid19_Interactivemap.models.LocationStats;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats location = new LocationStats();
            location.setState(record.get("Province/State"));
            System.out.println(record.get("Province/State"));
            location.setCountry(record.get("Country/Region"));
            location.setLatitude(Double.parseDouble(record.get("Lat")));
            location.setLongitude(Double.parseDouble(record.get("Long")));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevdaycases = Integer.parseInt(record.get(record.size()-2));
            location.setDiffFromPrevDay(latestCases - prevdaycases);
            location.setLatestTotalCases(latestCases);
            System.out.println(location);
            newStats.add(location);
        }

        this.allStats = newStats;

    }
}
