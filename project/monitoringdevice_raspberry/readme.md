# Dispositivo di Monitoraggio

Questa è la versione del MonitoringDevice che funzionerà sul Raspberry Pi per lo Sprint2 e Sprint3 del progetto.

Per connetterlo al contesto remoto, ricorda di cambiare l'indirizzo IP nel file sorgente monitoringdevice.qak.

## Perché serve un'implementazione diversa?

Per semplicità si è scelto di mantenere il codice del MonitoringDevice all'[interno dello sprint3](../sprint3/src/monitoringdevice.qak) per funzionare in modalità "mock" per facilitare lo sviluppo e il testing. Il codice presente in questa cartella invece è stato adattato per funzionare su un Raspberry Pi. 

Mantenendo queste due versioni, risulta più semplice gestire lo sviluppo del codice e la sua integrazione con il dispositivo esterno.
