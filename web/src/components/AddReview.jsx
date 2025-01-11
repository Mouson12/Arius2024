import React, { useState } from 'react';

const AddReview = () => {
    const [rating, setRating] = useState(0); // Ocena użytkownika
    const [description, setDescription] = useState('');
    const [repairOrderId, setRepairOrderId] = useState(''); // ID zamówienia naprawy
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Walidacja pól
        if (!rating || !description || !repairOrderId) {
            setMessage('All fields are required.');
            return;
        }

        // Automatyczne ustawienie daty wysyłki oceny
        const currentDate = new Date().toISOString();

        // Przygotowanie danych do wysłania
        const reviewData = {
            repair_order_id: repairOrderId,
            rating: rating,
            comment: description,
            date: currentDate, // Data dodana automatycznie
        };

        try {
            const token = localStorage.getItem('token');
            if (!token) {
                setMessage('User is not authenticated. Please log in.');
                return;
            }

            const response = await fetch('http://157.90.162.7:5001/api/ratings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(reviewData),
            });

            if (response.ok) {
                const data = await response.json();
                setMessage(`Rating submitted successfully.`);
                setRating(0);
                setDescription('');
                setRepairOrderId('');
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error submitting rating:', error);
            setMessage('An unexpected error occurred. Please try again later.');
        }
    };

    // Funkcja do ustawiania oceny
    const handleStarClick = (star) => {
        setRating(star);
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px', padding: '0px 20px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold', color: 'white' }}>
                        OCENA:
                    </label>
                    <div style={{ display: 'flex', marginBottom: '15px', gap: '5px', fontSize: '24px' }}>
                        {[...Array(10)].map((_, index) => {
                            const starNumber = index + 1;
                            return (
                                <span
                                    key={index}
                                    onClick={() => handleStarClick(starNumber)}
                                    style={{
                                        cursor: 'pointer',
                                        color: starNumber <= rating ? 'gold' : 'gray',
                                    }}
                                >
                                    ★
                                </span>
                            );
                        })}
                    </div>
                    <p style={{ color: 'white' }}>Wybrana ocena: {rating}</p>
                </div>
                <div style={{ marginBottom: '15px', padding: '0px 20px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold', color: 'white' }}>
                        ID Zamówienia Naprawy:
                    </label>
                    <input
                        type="text"
                        value={repairOrderId}
                        onChange={(e) => setRepairOrderId(e.target.value)}
                        placeholder="Wpisz ID zamówienia naprawy"
                        required
                        style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '5px', fontSize: '16px' }}
                    />
                </div>
                <div style={{ marginBottom: '15px', padding: '0px 20px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold', color: 'white' }}>
                        KOMENTARZ:
                    </label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Wpisz komentarz"
                        required
                        style={{ width: '100%', padding: '10px', border: '1px solid #ccc', borderRadius: '5px', fontSize: '16px' }}
                    />
                </div>
                <div style={{ marginBottom: '15px', paddingLeft: '20px' }}>
                    <button type="submit" style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}>
                        Zapisz
                    </button>
                </div>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default AddReview;
