import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';
import SpectacolTable from './SpectacolTable';
import SpectacolForm from './SpectacolForm';
import { GetSpectacole, DeleteSpectacol, AddSpectacol, UpdateSpectacol } from './utils/rest-calls';
import './SpectacolApp.css';

export default function SpectacolApp() {
    const [spectacole, setSpectacole] = useState([]);
    const [selectedSpectacol, setSelectedSpectacol] = useState(null);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            onConnect: () => {
                console.log('Connected to WebSocket');
                client.subscribe('/topic/spectacole', () => {
                    GetSpectacole()
                        .then(data => setSpectacole(data))
                        .catch(console.error);
                });
            },
            onStompError: (frame) => {
                console.error('STOMP error:', frame.headers['message'], frame.body);
            },
            debug: (str) => console.log('[STOMP DEBUG]', str),
            reconnectDelay: 5000,
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    useEffect(() => {
        GetSpectacole()
            .then(data => setSpectacole(data))
            .catch(error => console.error('Fetch error:', error));
    }, []);

    function addFunc(newSpectacol) {
        if (selectedSpectacol) {
            UpdateSpectacol(selectedSpectacol.id, newSpectacol)
                .then(() => GetSpectacole())
                .then(data => {
                    setSpectacole(data);
                    setSelectedSpectacol(null);
                    alert('Spectacol updated successfully!');
                })
                .catch(error => console.error('Update error:', error));
        } else {
            AddSpectacol(newSpectacol)
                .then(() => GetSpectacole())
                .then(data => {
                    setSpectacole(data);
                    alert('Spectacol added successfully!');
                })
                .catch(error => console.error('Add error:', error));
        }
    }

    function deleteFunc(id) {
        DeleteSpectacol(id)
            .then(() => GetSpectacole())
            .then(data => {
                setSpectacole(data);
                alert('Spectacol deleted successfully!');
            })
            .catch(error => console.error('Delete error:', error));
    }

    function handleRowClick(spectacol) {
        setSelectedSpectacol(spectacol);
    }

    function handleCancel() {
        setSelectedSpectacol(null);
    }

    return (
        <div className="SpectacolApp">
            <h1 className="SpectacolApp-h1">Gestiune Festival Spectacole</h1>
            <SpectacolForm
                addFunc={addFunc}
                selectedSpectacol={selectedSpectacol}
                onCancel={handleCancel}
            />
            <br />
            <SpectacolTable
                spectacoleList={spectacole}
                deleteFunc={deleteFunc}
                onRowClick={handleRowClick}
                selectedId={selectedSpectacol?.id}
            />
        </div>
    );
}