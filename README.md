# Zadaniok – aplikacja do zarządzania zadaniami (Android)

---

## 1. Opis aplikacji

**Cel aplikacji:**  
Celem aplikacji Zadaniok jest umożliwienie użytkownikowi prostego i wygodnego zarządzania codziennymi zadaniami wraz z możliwością ustawiania terminów wykonania oraz otrzymywania przypomnień w postaci powiadomień.

**Grupa docelowa:**  
Aplikacja skierowana jest do osób, które chcą w łatwy sposób planować swoje obowiązki, studentów, uczniów oraz użytkowników prywatnych potrzebujących prostej listy zadań.

**Charakter aplikacji:**  
Mobilna aplikacja typu To-Do działająca lokalnie z dodatkową komunikacją z API.

---

## 2. Wybrane technologie i uzasadnienie

- Kotlin – nowoczesny język zalecany do tworzenia aplikacji Android  
- Jetpack Compose (Material 3) – nowoczesny system budowania interfejsów  
- MVVM (Model–View–ViewModel) – czytelny podział odpowiedzialności  
- DataStore – trwały zapis danych lokalnych  
- WorkManager – planowanie powiadomień i przypomnień  
- Retrofit + Gson – komunikacja z API i parsowanie JSON  

Wybrane technologie zapewniają stabilność, czytelność kodu oraz zgodność z aktualnymi standardami Android.

---

## 3. Architektura aplikacji

Projekt wykorzystuje architekturę MVVM:

- UI (Compose) – warstwa widoku  
- ViewModel – logika biznesowa  
- Repository – pośrednik między danymi a ViewModel  
- Źródła danych – DataStore (lokalne) oraz API (cytat dnia)  

Zastosowane wzorce projektowe:
- MVVM  
- Repository Pattern  

Schemat uproszczony:

UI → ViewModel → Repository → (DataStore / API)

---

## 4. Opis zaimplementowanych funkcjonalności

- Dodawanie zadania (tytuł, opis)  
- Ustawianie terminu wykonania  
- Oznaczanie zadania jako Zrobione / Do zrobienia  
- Usuwanie zadania z potwierdzeniem  
- Powiadomienia o terminach  
- Oznaczanie zadań przeterminowanych (PO TERMINIE)  
- Licznik liczby zadań  
- Cytat dnia pobierany z internetu  
- Ręczne odświeżanie cytatu  

---

## 5. Zrzuty ekranu

W katalogu `docs/screenshots` znajdują się przykładowe zrzuty ekranu:

- Lista zadań  
- Dodawanie zadania  
- Powiadomienie systemowe  
- Cytat dnia  

---

## 6. Instrukcja uruchomienia

1. Sklonuj repozytorium:

git clone https://github.com/wrx91510/Zadaniok.git
2. Otwórz projekt w Android Studio  
3. Wykonaj Sync Gradle  
4. Uruchom aplikację na emulatorze lub urządzeniu  

---

## 7. Napotkane problemy i rozwiązania

- Problem z pobieraniem cytatu z API Quotable  
  → Zmieniono API na ZenQuotes  

- Problemy z pozycjonowaniem przycisku „+”  
  → Zastosowano dodatkowy padding  

- Problemy z powiadomieniami na nowszych wersjach Android  
  → Dodano permission POST_NOTIFICATIONS  

---

## 8. Możliwości rozwoju

- Kategorie zadań  
- Wyszukiwanie i filtrowanie  
- Synchronizacja z chmurą  
- Konta użytkowników  
- Tryb ciemny  

---

## Autor

Szymon Dobrzański
