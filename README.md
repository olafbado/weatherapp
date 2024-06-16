### Wprowadzenie

WeatherApp to prosta aplikacja napisana w Javie, która pobiera i wyświetla aktualne dane pogodowe dla wybranej lokalizacji. Aplikacja korzysta z API WeatherAPI do pobierania informacji o pogodzie i prezentuje je w graficznym interfejsie użytkownika (GUI) z ikonami i wykresami.
Wymagania wstępne

Przed rozpoczęciem pracy upewnij się, że masz:
- Zainstalowany Java Development Kit (JDK) 8 lub nowszy
- Zainstalowany Apache Maven
- Klucz API z WeatherAPI (zamień YOUR_API_KEY w kodzie na swój rzeczywisty klucz)

### Instalacja

1. Sklonuj repozytorium lub pobierz pliki projektu:

`git clone https://github.com/olafbado/weatherapp.git`
`cd weatherapp`

2. Dodaj swój klucz API:

Otwórz plik WeatherApp.java i zamień API_KEY na swój rzeczywisty klucz API w klasie ApiService.

1. Zbuduj projekt przy użyciu Maven:

`mvn compile`

### Użycie

Aby uruchomić aplikację, użyj następującego polecenia Maven:

`mvn exec:java -Dexec.mainClass="com.example.weatherapp.WeatherApp"`

Po uruchomieniu aplikacji pojawi się okno GUI, które pozwoli Ci wpisać lokalizację i pobrać dane pogodowe.
Kroki użycia aplikacji:

1. Wprowadzenie lokalizacji:

    W polu tekstowym "Location" wpisz nazwę miasta lub lokalizacji, dla której chcesz sprawdzić pogodę.

2. Pobieranie danych pogodowych:

    Kliknij przycisk "Get Weather". Aplikacja pobierze aktualne dane pogodowe dla podanej lokalizacji.

3. Wyświetlanie wyników:

    W obszarze tekstowym pojawią się informacje o lokalizacji, temperaturze, opisie pogody, wilgotności i prędkości wiatru.

    Poza tym, w oknie aplikacji wyświetli się ikona symbolizująca aktualny stan pogody oraz wykres przedstawiający dane takie jak temperatura, wilgotność i prędkość wiatru.