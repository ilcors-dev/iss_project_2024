# Mqtt

L'applicazione, a partire da Sprint2, utilizza un broker mqtt locale per evitare le restrizioni di connessione imposte dai broker pubblici.

## Esecuzione standalone

> **Nota**: Docker & Docker Compose devono essere installati sul sistema.

Per avviare il broker, eseguire semplicemente il seguente comando nella cartella corrente:

```bash
docker-compose -f docker-compose.yml up
```


> **Nota**: Il broker sarà disponibile all'indirizzo `localhost:1883`.
