import React from 'react';

function SpectacolRow({ spectacol, deleteFunc, onRowClick, isSelected }) {
    return (
        <tr
            onClick={() => onRowClick(spectacol)}
            style={{ backgroundColor: isSelected ? '#e0f0ff' : 'transparent' }}
        >
            <td>{spectacol.id}</td>
            <td>{spectacol.numeArtist}</td>
            <td>{new Date(spectacol.dataSpectacol).toLocaleString()}</td>
            <td>{spectacol.locSpectacol}</td>
            <td>{spectacol.nrLocuriDisponibile}</td>
            <td>{spectacol.nrLocuriOcupate}</td>
            <td>
                <button onClick={(e) => {
                    e.stopPropagation();
                    deleteFunc(spectacol.id);
                }}>Delete</button>
            </td>
        </tr>
    );
}
export default SpectacolRow;