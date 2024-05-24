# Film

## Beschreibung
Dieses Projekt implementiert eine Backend-API für die Verwaltung von Filmdaten. Es umfasst Dienste zum Lesen, Erstellen, Aktualisieren und Löschen von Filminformationen. Die Datenstruktur basiert auf zwei miteinander verbundenen Tabellen, `Film` und `Genre`, wobei `Film` die Haupttabelle ist.

## Visuals
![Screenshot 2024-05-24 141613](https://github.com/greisingerd-bzz/Film/assets/114403483/006a2a40-bc27-4521-9ec2-5fcbe93412b8)
![FilmKlassendiagram2](https://github.com/greisingerd-bzz/Film/assets/114403483/97b2a297-afe4-4c8e-8dfb-2454689479af)
![Screenshot 2024-05-24 135034](https://github.com/greisingerd-bzz/Film/assets/114403483/96568101-156f-4111-ad5b-993a20190d8b)


## Validierungsregeln
1. **Erscheinungsjahr**: Das Datum muss in der Vergangenheit liegen.
2. **Bewertung**: Die Bewertung eines Films muss zwischen 0 und 10 liegen.
3. **Laufzeit**: Die Laufzeit des Films muss eine positive Ganzzahl sein.

## Berechtigungsmatrix
- **Admin**: Kann lesen, erstellen, aktualisieren und löschen.
- **Poster**: Kann lesen und erstellen.
- **Gast**: Kann nur lesen.

## OpenAPI Dokumentation der Services (Resourcen)
- `GET /api/film`: Gibt alle Filme zurück.
- `POST /api/film`: Erstellt einen neuen Film.
- `PUT /api/film/{id}`: Aktualisiert einen vorhandenen Film.
- `DELETE /api/film/{id}`: Löscht einen Film.

## Autor
- **Name**: Dennis Greisinger

## Zusammenfassung
Dieses Projekt wurde als Teil der Kursarbeit für das Modul 295 entwickelt. Es demonstriert die Anwendung moderner Softwareentwicklungspraktiken einschließlich REST-API-Design, Datenpersistenz, Unit-Testing und Authentifizierung.

## Anweisungen
Um das Projekt zu starten, führen Sie folgende Schritte durch:
1. Klone das Repository: `git clone [URL]`
2. Installiere die Abhängigkeiten: `npm install` (falls Node.js verwendet wird)
3. Starte den Server: `npm start` (falls Node.js verwendet wird)

Bitte beachten Sie die beigefügte `requirements.txt` oder `package.json` für technische Details und Abhängigkeiten.

## Lizenz
Dieses Projekt ist unter der MIT Lizenz veröffentlicht.
