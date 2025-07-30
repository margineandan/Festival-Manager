import React from 'react';
import SpectacolRow from './SpectacolRow';
import './SpectacolApp.css';

export default function SpectacolTable({ spectacoleList, deleteFunc, onRowClick, selectedId }) {
    return (
        <div className="SpectacolTable">
            <table className="center">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Artist</th>
                    <th>Data</th>
                    <th>Locatie</th>
                    <th>Locuri Disponibile</th>
                    <th>Locuri Ocupate</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {spectacoleList.map(spectacol => (
                    <SpectacolRow
                        key={spectacol.id}
                        spectacol={spectacol}
                        deleteFunc={deleteFunc}
                        onRowClick={onRowClick}
                        isSelected={spectacol.id === selectedId}
                    />
                ))}
                </tbody>
            </table>
        </div>
    );
}