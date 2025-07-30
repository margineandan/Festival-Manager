import React, { useState, useEffect } from 'react';

export default function SpectacolForm({ addFunc, selectedSpectacol, onCancel }) {
    const [formData, setFormData] = useState({
        numeArtist: '',
        dataSpectacol: '',
        locSpectacol: '',
        nrLocuriDisponibile: 0,
        nrLocuriOcupate: 0
    });

    useEffect(() => {
        if (selectedSpectacol) {
            setFormData({
                numeArtist: selectedSpectacol.numeArtist,
                dataSpectacol: selectedSpectacol.dataSpectacol,
                locSpectacol: selectedSpectacol.locSpectacol,
                nrLocuriDisponibile: selectedSpectacol.nrLocuriDisponibile,
                nrLocuriOcupate: selectedSpectacol.nrLocuriOcupate
            });
        } else {
            setFormData({
                numeArtist: '',
                dataSpectacol: '',
                locSpectacol: '',
                nrLocuriDisponibile: 0,
                nrLocuriOcupate: 0
            });
        }
    }, [selectedSpectacol]);

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({
            ...formData,
            nrLocuriDisponibile: Number(formData.nrLocuriDisponibile),
            nrLocuriOcupate: Number(formData.nrLocuriOcupate)
        });
    }

    function handleChange(e) {
        setFormData({
            ...formData,
            [e.target.name]:
                e.target.type === 'number'
                    ? e.target.valueAsNumber
                    : e.target.value
        });
    }

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="numeArtist">
                Nume Artist:
            </label>
            <input
                id="numeArtist"
                name="numeArtist"
                type="text"
                value={formData.numeArtist}
                onChange={handleChange}
                required
            />

            <label htmlFor="dataSpectacol">
                Data Spectacol:
            </label>
            <input
                id="dataSpectacol"
                name="dataSpectacol"
                type="datetime-local"
                value={formData.dataSpectacol}
                onChange={handleChange}
                required
            />

            <label htmlFor="locSpectacol">
                Locatie:
            </label>
            <input
                id="locSpectacol"
                name="locSpectacol"
                type="text"
                value={formData.locSpectacol}
                onChange={handleChange}
                required
            />

            <label htmlFor="nrLocuriDisponibile">
                Locuri Disponibile:
            </label>
            <input
                id="nrLocuriDisponibile"
                name="nrLocuriDisponibile"
                type="number"
                min="0"
                value={formData.nrLocuriDisponibile}
                onChange={handleChange}
                required
            />

            <label htmlFor="nrLocuriOcupate">
                Locuri Ocupate:
            </label>
            <input
                id="nrLocuriOcupate"
                name="nrLocuriOcupate"
                type="number"
                min="0"
                value={formData.nrLocuriOcupate}
                onChange={handleChange}
                required
            />

            <div className="form-buttons">
                <input
                    type="submit"
                    value={
                        selectedSpectacol
                            ? 'Update Spectacol'
                            : 'Add Spectacol'
                    }
                />
                {selectedSpectacol && (
                    <button type="button" onClick={onCancel}>
                        Cancel
                    </button>
                )}
            </div>
        </form>
    );
}
