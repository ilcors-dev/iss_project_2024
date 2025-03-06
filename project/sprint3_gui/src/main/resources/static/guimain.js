var socket;

function connect() {
	document.querySelector('h1').style.color = 'blue';
	// Ottieni l'host e imposta il percorso del WebSocket
	var host = document.location.host;
	var pathname = '/';
	var addr = 'ws://' + host + pathname + 'WebRobot24Gui';

	// Chiudi la connessione WebSocket esistente, se presente
	if (socket != null) {
		socket.close();
	}

	// Inizializza la nuova connessione WebSocket
	socket = new WebSocket(addr);

	socket.onopen = function () {
		console.log('Connected to WebSocket');
	};

	socket.onmessage = function (event) {
		// Estrarre il contenuto del messaggio dalla WebSocket
		const data = event.data;
		console.log(data);

		// Regex per estrarre i dati da data
		const wasteStorageMatch = data.match(/RP: (\d+)/);
		const incineratorStatusMatch = data.match(/incinerator: (\w+)/);
		const robotLocationMatch = data.match(/robot: ([\w\s]+)/);
		const ashStorageMatch = data.match(/ash: (\d+)/);

		// Aggiorna i campi della tabella con i dati estratti
		if (wasteStorageMatch) {
			document.getElementById('waste-storage').textContent =
				wasteStorageMatch[1];
		}
		if (incineratorStatusMatch) {
			document.getElementById('incinerator-status').textContent =
				incineratorStatusMatch[1];
		}
		if (robotLocationMatch) {
			document.getElementById('robot-location').textContent =
				robotLocationMatch[1];
		}
		if (ashStorageMatch) {
			document.getElementById('ash-storage').textContent = ashStorageMatch[1];
		}
	};

	socket.onerror = function (error) {
		console.error('WebSocket Error:', error);
	};

	socket.onclose = function () {
		console.log('WebSocket connection closed');
	};
}
