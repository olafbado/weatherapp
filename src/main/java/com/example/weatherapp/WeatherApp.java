package com.example.weatherapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class WeatherApp {
    public static void main(String[] args) {
        WeatherAppUI app = new WeatherAppUI();
        app.show();
    }
}

class WeatherData {
    private String location;
    private double temperature;
    private String description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class CurrentWeather extends WeatherData {
    private double humidity;
    private double windSpeed;

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}

class ApiService {
    private final String apiKey = "b0cab9b492804425a9a72753241506";
    private final String apiUrl = "http://api.weatherapi.com/v1/current.json";

    public String getWeatherData(String location) throws IOException {
        URL url = new URL(apiUrl + "?key=" + apiKey + "&q=" + location);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response.toString();
    }

    public String getWeatherIconUrl(String location) throws IOException {
        String response = getWeatherData(location);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject current = jsonObject.getJSONObject("current");
        return "http:" + current.getJSONObject("condition").getString("icon");
    }
}

class WeatherService {
    public ApiService apiService;

    public WeatherService() {
        this.apiService = new ApiService();
    }

    public CurrentWeather getCurrentWeather(String location) {
        try {
            String response = apiService.getWeatherData(location);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject current = jsonObject.getJSONObject("current");

            CurrentWeather weather = new CurrentWeather();
            weather.setLocation(location);
            weather.setTemperature(current.getDouble("temp_c"));
            weather.setDescription(current.getJSONObject("condition").getString("text"));
            weather.setHumidity(current.getDouble("humidity"));
            weather.setWindSpeed(current.getDouble("wind_kph"));

            return weather;
        } catch (Exception e) {
            ErrorHandler.handleError(e);
            return null;
        }
    }
}

class WeatherAppUI {
    private JFrame frame;
    private JTextField locationField;
    private JTextArea weatherArea;
    private JLabel weatherIcon;
    private WeatherService weatherService;

    public WeatherAppUI() {
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        locationField = new JTextField(20);
        JButton fetchButton = new JButton("Get Weather");
        weatherArea = new JTextArea(10, 30);
        weatherArea.setEditable(false);
        weatherIcon = new JLabel();

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(fetchButton);
        panel.add(weatherIcon);
        panel.add(new JScrollPane(weatherArea));

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        weatherService = new WeatherService();
    }

    public void show() {
        frame.setVisible(true);
    }

    private void fetchWeather() {
        String location = locationField.getText();
        CurrentWeather weather = weatherService.getCurrentWeather(location);
        if (weather != null) {
            weatherArea.setText("Location: " + weather.getLocation() +
                    "\nTemperature: " + weather.getTemperature() + "°C" +
                    "\nDescription: " + weather.getDescription() +
                    "\nHumidity: " + weather.getHumidity() + "%" +
                    "\nWind Speed: " + weather.getWindSpeed() + " kph");

            try {
                String iconUrl = weatherService.apiService.getWeatherIconUrl(location);
                BufferedImage icon = ImageIO.read(new URL(iconUrl));
                weatherIcon.setIcon(new ImageIcon(icon));
            } catch (IOException e) {
                ErrorHandler.handleError(e);
            }

            JPanel chartPanel = createChartPanel(weather);
            frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
            frame.revalidate();
        } else {
            weatherArea.setText("Error fetching weather data");
        }
    }

    private JPanel createChartPanel(CurrentWeather weather) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(weather.getTemperature(), "Temperature", "Current");
        dataset.addValue(weather.getHumidity(), "Humidity", "Current");
        dataset.addValue(weather.getWindSpeed(), "Wind Speed", "Current");

        JFreeChart chart = ChartFactory.createBarChart(
                "Weather Data",
                "Category",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        return new ChartPanel(chart);
    }
}

class ErrorHandler {
    public static void handleError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
