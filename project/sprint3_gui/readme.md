## Sprint3 GUI

Questa cartella contiene i file relativi alla GUI del progetto, resa disponibile a partire dallo Sprint3.

È un'applicazione Spring, già utilizzata durante il corso che tramite il protocollo COAP si mette in ascolto degli `updateResource` lanciati dagli attori presenti nel sistema.

La connessione alla pagina html per la visualizzazione dei dati avviene attraverso WebSocket.

### Esecuzione

Per eseguire l'applicazione è sufficiente eseguire il comando nella cartella corrente:

```bash
gradle run
```

> Si noti che l'applicazione principale (cartella ../sprint3) deve essere in esecuzione per poter visualizzare i dati.
