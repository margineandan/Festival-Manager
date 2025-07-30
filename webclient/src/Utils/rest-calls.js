import { FESTIVAL_SPECTACOLE_BASE_URL } from './consts';

function status(response) {
    console.log('Response status: ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

export function GetSpectacole() {
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    let myInit = {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    };

    console.log('Fetching spectacole from ' + FESTIVAL_SPECTACOLE_BASE_URL);

    return fetch(FESTIVAL_SPECTACOLE_BASE_URL, myInit)
        .then(status)
        .then(response => response.json())
        .then(data => {
            console.log('Successfully fetched spectacole:', data);
            return data;
        }).catch(error => {
            console.error('Failed to fetch spectacole:', error);
            return Promise.reject(error);
        });
}

export function DeleteSpectacol(id) {
    console.log('Deleting spectacol with ID:', id);
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");

    const spectacolDelUrl = FESTIVAL_SPECTACOLE_BASE_URL + '/' + id;
    console.log('Delete URL:', spectacolDelUrl);

    return fetch(spectacolDelUrl, {
        method: 'DELETE',
        headers: myHeaders,
        mode: 'cors'
    })
        .then(status)
        .then(response => {
            console.log('Delete status:', response.status);
        }).catch(error => {
            console.error('Delete failed:', error);
            return Promise.reject(error);
        });
}

export function AddSpectacol(spectacol) {
    console.log('Adding new spectacol:', JSON.stringify(spectacol));
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    return fetch(FESTIVAL_SPECTACOLE_BASE_URL, {
        method: 'POST',
        headers: myHeaders,
        mode: 'cors',
        body: JSON.stringify(spectacol)
    })
        .then(status)
        .then(response => response.json())
        .then(data => {
            console.log('Successfully added spectacol:', data);
            return data;
        }).catch(error => {
            console.error('Add failed:', error);
            return Promise.reject(error);
        });
}

export function UpdateSpectacol(id, spectacol) {
    console.log(`Updating spectacol ID ${id}:`, JSON.stringify(spectacol));
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    const url = FESTIVAL_SPECTACOLE_BASE_URL + '/' + id;

    const updatedSpectacol = { ...spectacol, id: id };

    return fetch(url, {
        method: 'PUT',
        headers: myHeaders,
        mode: 'cors',
        body: JSON.stringify(updatedSpectacol)
    })
        .then(status)
        .then(response => response.json())
        .then(data => {
            console.log('Successfully updated spectacol:', data);
            return data;
        }).catch(error => {
            console.error('Update failed:', error);
            return Promise.reject(error);
        });
}