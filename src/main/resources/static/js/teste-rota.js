let mapa;
let directionsService;
let directionsRenderer;

// 1. Primeiro: Carrega a chave da API do backend
fetch('/api/maps-key')
    .then(response => {
        if (!response.ok) throw new Error('Erro ao carregar chave do Google Maps');
        return response.text();
    })
    .then(key => {
        // 2. Carrega a API do Google Maps com a chave obtida
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${key}&callback=inicializarMapa`;
        script.async = true;
        script.defer = true;
        script.onerror = () => console.error('Erro ao carregar Google Maps API');
        document.head.appendChild(script);
    })
    .catch(error => {
        console.error('Falha crítica:', error);
        alert('Não foi possível carregar o mapa. Recarregue a página.');
    });

// 3. Função chamada pelo Google Maps API quando estiver pronta
function inicializarMapa() {
    mapa = new google.maps.Map(document.getElementById('mapa'), {
        center: { lat: -16.4664, lng: -54.6359 }, // Centro em Rondonópolis
        zoom: 13
    });

    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer({ map: mapa });

    // 4. Configura o formulário (só depois do mapa estar pronto)
    document.getElementById('form-rota').addEventListener('submit', (e) => {
        e.preventDefault();

        const origem = document.getElementById('origem').value + ', Rondonópolis, MT';
        const destino = document.getElementById('destino').value + ', Rondonópolis, MT';

        directionsService.route(
            {
                origin: origem,
                destination: destino,
                travelMode: 'DRIVING'
            },
            (result, status) => {
                if (status === 'OK') {
                    directionsRenderer.setDirections(result);
                    document.getElementById('tempo').textContent = result.routes[0].legs[0].duration.text;
                    document.getElementById('resultado').classList.remove('d-none');
                } else {
                    alert('Erro ao calcular rota: ' + status);
                }
            }
        );
    });
}