import React, { useState } from 'react';

const CreateRepairOrder = () => {
    const [vehicleModel, setVehicleModel] = useState('');
    const [description, setDescription] = useState('');
    const [appointmentDate, setAppointmentDate] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Walidacja pól
        if (!vehicleModel || !description || !appointmentDate) {
            setMessage('All fields are required.');
            return;
        }

        // Formatowanie daty do ISO (jeśli potrzebne)
        const formattedAppointmentDate = new Date(appointmentDate).toISOString().split('.')[0]; // YYYY-MM-DDTHH:mm:ss

        // Przygotowanie danych do wysłania
        const repairOrderData = {
            vehicle_model: vehicleModel,
            description: description,
            appointment_date: formattedAppointmentDate,
        };

        try {
            // Pobranie tokenu JWT z LocalStorage
            const token = localStorage.getItem('token');
            if (!token) {
                setMessage('User is not authenticated. Please log in.');
                return;
            }

            // Wysłanie żądania POST
            const response = await fetch('http://157.90.162.7:5001/api/repair_orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(repairOrderData),
            });

            if (response.ok) {
                const data = await response.json();
                setMessage(`Order created successfully. Order ID: ${data.order_id}`);
                // Resetowanie pól formularza
                setVehicleModel('');
                setDescription('');
                setAppointmentDate('');
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error creating repair order:', error);
            setMessage('An unexpected error occurred. Please try again later.');
        }
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>MODEL SAMOCHODU:</label>
                    <input
                        type="text"
                        value={vehicleModel}
                        onChange={(e) => setVehicleModel(e.target.value)}
                        placeholder="Enter vehicle model"
                        required
                    />
                </div>
                <div>
                    <label>OPIS SERWISU:</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter description of the repair"
                        required
                    />
                </div>
                <div>
                    <label>DATA SERWISU:</label>
                    <input
                        type="datetime-local"
                        value={appointmentDate}
                        onChange={(e) => setAppointmentDate(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Create Order</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default CreateRepairOrder;
