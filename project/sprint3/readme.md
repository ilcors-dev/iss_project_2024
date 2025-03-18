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

Per eseguire l'applicazione standalone, è necessario scaricare 4 componenti, disponibili in [questa pagina](https://github.com/ilcors-dev/iss_project_2024/releases/tag/v1.0.0). Dopodiché, unzippare le cartelle, aprire aprire 3 terminali e lanciare i seguenti comandi:

#### Terminal 1: WeighingDevice

| Operating System | Command                                             |
| :--------------- | :-------------------------------------------------- |
| MacOS/Linux      | `cd weighingDevice-1.0 && ./bin/weighingDevice`     |
| Windows          | `cd weighingDevice-1.0 && ./bin/weighingDevice.bat` |

#### Terminal 2: MonitoringDevice

| Operating System | Command                                                 |
| :--------------- | :------------------------------------------------------ |
| MacOS/Linux      | `cd monitoringDevice-1.0 && ./bin/monitoringDevice`     |
| Windows          | `cd monitoringDevice-1.0 && ./bin/monitoringDevice.bat` |

#### Terminal 3: Wis24

| Operating System | Command                           |
| :--------------- | :-------------------------------- |
| MacOS/Linux      | `cd wis24-1.0 && ./bin/wis24`     |
| Windows          | `cd wis24-1.0 && ./bin/wis24.bat` |

#### Terminal 4: GUI

| Operating System | Command                                         |
| :--------------- | :---------------------------------------------- |
| MacOS/Linux      | `cd ./sprint3_gui-1.0 && ./bin/sprint3_gui`     |
| Windows          | `cd ./sprint3_gui-1.0 && ./bin/sprint3_gui.bat` |


### Con Raspberry

Sul Raspberry, verrà eseguito il **MonitoringDevice (Sonar + Led)**.

> **Nota**: Per questa modalità è necessario pre-configurare gli indirizzi IP dei dispositivi all'interno del codice sorgente. In particolare, è necessario modificare i seguenti file:
> - ./sprint3/src/system.qak (linea 56-57)
> - ./monitoringdevice_raspberry/src/monitoringdevice.qak (linea 13)

> È quindi necessario, per via dei limiti del linguaggio QAK, avere eclipse configurato per poter compilare il codice sorgente QAK.

Una volta configurati gli indirizzi IP, è possibile eseguire gli stessi comandi della modalità standalone per il **[Terminal 1](#terminal-1-weighingdevice)** e **[Terminal 4](#terminal-4-gui)**.

Dopodiché, sul raspberry (via ssh o dall'ambiente grafico) è necessario lanciare il seguente comando:

```bash
cd monitoringdevice-1.0 && ./bin/monitoringdevice
```

Per eseguire **Wis24** una volta configurati gli indirizzi IP, è necessario lanciare il comando:

```bash
gradle run
```

Oppure, in alternativa, si può creare la cartella `wis24-1.0` facendo la build del progetto con il comando:

```bash
./gradlew distZip
```

e ripetere i comandi descritti nella sezione **[Terminal 3](#terminal-3-wis24)**.
