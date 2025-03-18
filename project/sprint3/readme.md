# Sprint 3

Questo Sprint rappresenta la versione finale dell'applicazione, composta da 3 componenti principali:
- **[WIS](./src/system.qak)**: controllore centrale del sistema che si occupa di coordinare le varie componenti del sistema (Incinerator, [WeighingDevice](./src/scale.qak), [MonitoringDevice](./src/monitoringdevice.qak), [OpRobot - Virtuale](../html/VirtualRobot23.html))
- **[MonitoringDevice](./src/monitoringdevice.qak)**: dispositivo di monitoraggio eseguibile sul Raspberry Pi (vedi [Esecuzione Sprint3 (Demo)](#esecuzione-sprint3-demo)) che gestisce la cenere prodotta dall'inceneritore e il dispositivo di allarme (led)
- **[WeighingDevice](./src/scale.qak)**: dispositivo per la gestione degli RP (Roll Packets) in entrata nel sistema

## Esecuzione Sprint 3 (Demo)

Ci sono varie modalità di esecuzione dell'applicazione:
- standalone: per demo locali senza uso di Raspberry
- con Raspberry: per demo con Raspberry

### Prerequisiti

- Docker & Docker Compose installati sul sistema.

### Azioni preliminari

A prescindere dalla modalità di esecuzione, è necessario eseguire i seguenti passaggi per avviare:
- il broker MQTT
- il virtual robot

#### Broker MQTT

Entrare nella cartella `mqtt` e lanciare il comando:

```bash
docker-compose -f docker-compose.yml up
```

> Il broker sarà disponibile all'indirizzo `localhost:1883`.

#### Virtual Robot

Entrare nella cartella `unibo.basicrobot24` e lanciare il comando:

```bash
docker-compose -f basicrobot24.yaml up
```

> Il VirtualRobot sarà disponibile all'indirizzo `localhost:8090`.

### Standalone

Per eseguire l'applicazione standalone, è necessario scaricare 3 componenti, disponibili in [questa pagina](). Dopodiché, aprire aprire 3 terminali e lanciare i seguenti comandi:

- Terminal 1: WeighingDevice `./WeighingDevice`
- Terminal 2: MonitoringDevice `./MonitoringDevice`
- Terminal 3: MainApplication `./system`

### Con Raspberry

> **Nota**: Per questa modalità è necessario pre-configurare gli indirizzi IP dei dispositivi all'interno del codice sorgente. In particolare, è necessario modificare i seguenti file:
> - ./sprint3/src/system.qak (linea 56-57)
> - ./monitoringdevice_raspberry/src/monitoringdevice.qak (linea 13)

Una volta configurati gli indirizzi IP, è possibile eseguire gli stessi comandi della modalità standalone per il **Terminal 1** e il **Terminal 2**.

Dopodiché, sul raspberry (via ssh o dall'ambiente grafico) è necessario lanciare il seguente comando:

```bash
./monitoringdevice
```
