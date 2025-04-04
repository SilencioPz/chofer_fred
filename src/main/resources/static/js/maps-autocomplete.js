// maps-autocomplete.js
async function initMaps() {
    try {

        const response = await fetch('/api/agendar');
         method: 'POST',
             headers: {
                 'Content-Type': 'application/json'
             },
             body: JSON.stringify({
                 origem: "Rua A, 123",
                 destino: "Rua B, 456",
                 // outros campos necessários
             })
         });

        document.getElementById('map-loading').style.display = 'none';

        const placePicker = document.querySelector('gmpx-place-picker');
        placePicker.addEventListener('gmpx-placechange', () => {
            const place = placePicker.value;
            if (!place?.location) return;

            if (place.viewport) {
                map.innerMap.fitBounds(place.viewport);
            } else {
                map.center = place.location;
                map.zoom = 17;
            }
            marker.position = place.location;
        });

        loadingElement.style.display = 'none';

    } catch (error) {
        console.error("Erro no Google Maps:", error);
         document.getElementById('map-loading').innerHTML =
                    '<div class="alert alert-danger">Erro ao carregar o mapa</div>';
    }
}

if (document.getElementById('map-container')) {
    document.addEventListener('DOMContentLoaded', initMaps);
}