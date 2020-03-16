package io.puri.kingdom.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import models.LocationStats;

@Service
public class CoronaVirusDataService {
	
	public String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

	private List<LocationStats> allStats = new ArrayList<>();	
	public List<LocationStats> getAllStats() {
		return allStats;
	}
	public void setAllStats(List<LocationStats> allStats) {
		this.allStats = allStats;
	}
	
	public int getLatestCaseCount() {
		return allStats.stream().mapToInt(locationStat -> locationStat.getLatestCasesCount()).sum();
	}
	
	public int getPreviousCaseCount() {
		return allStats.stream().mapToInt(locationStat -> locationStat.getPreviousCasesCount()).sum();
	}
	
	public String fetchData() throws IOException {
		
		URL url = new URL(DATA_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
		BufferedReader in = new BufferedReader(inputStream);
		String line;
		StringBuffer data = new StringBuffer();
		
		while((line = in.readLine()) != null) {
			data.append(line);
			data.append("\n");
		}
		in.close();
		return data.toString();
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * * ")
	public void fetchCoronaVirusData() {
		List<LocationStats> newStats = new ArrayList<>();

		try {
			String data = fetchData();
			
			StringReader reader = new StringReader(data.toString());
			Iterable<CSVRecord> records;
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
			
			for (CSVRecord record : records) {
			    String state = record.get("Province/State");
			    String country = record.get("Country/Region");
			    
			    LocationStats locationStat = new LocationStats();
			    
			    locationStat.setState(state.equals("") ? "ALL_STATES" : state);
			    locationStat.setCountry(country);
			    locationStat.setLatestCasesCount(Integer.parseInt(record.get(record.size() - 1)));
			    locationStat.setPreviousCasesCount(Integer.parseInt(record.get(record.size() - 2)));
			    
			    newStats.add(locationStat);
			}
			this.allStats = newStats;
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
