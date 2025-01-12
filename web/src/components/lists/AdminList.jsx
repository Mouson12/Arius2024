import React, { useEffect, useState } from "react";
import "./ScrollableList.css";

const AdminList = () => {
    const [appointmentList, setAppointmentList] = useState([]); // Stan na dane z napraw
    const [services, setServices] = useState([]); // Stan na dane z listy usług
    const [selectedServices, setSelectedServices] = useState({}); // Mapa przypisanych usług
    const [error, setError] = useState(null); // Obsługa błędów
    const [loading, setLoading] = useState(true); // Obsługa ładowania danych

    // Funkcja pobierająca listę napraw
    const fetchAppointmentList = async () => {
        const token = localStorage.getItem("token");

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/repair_orders", {
                method: "GET",
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error("Nieautoryzowany. Zaloguj się ponownie.");
                } else {
                    throw new Error(`Błąd serwera: ${response.statusText}`);
                }
            }

            const data = await response.json();
            setAppointmentList(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funkcja pobierająca listę usług
    const fetchServices = async () => {
        try {
            const response = await fetch("http://157.90.162.7:5001/api/services", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error(`Błąd pobierania usług: ${response.statusText}`);
            }

            const data = await response.json();
            setServices(data);
        } catch (err) {
            setError(err.message);
        }
    };

    // Funkcja do obsługi przypisania usługi do naprawy
    const handleServiceChange = (orderId, serviceId) => {
        setSelectedServices((prev) => ({
            ...prev,
            [orderId]: serviceId,
        }));
    };

    // Funkcja zatwierdzania naprawy
    const completeRepair = async (orderId, report) => {
        const token = localStorage.getItem("token");

        if (!token) {
            alert("Brak tokenu. Zaloguj się ponownie.");
            return;
        }

        const serviceId = selectedServices[orderId];
        if (!serviceId) {
            alert("Wybierz usługę przed zakończeniem naprawy.");
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/repair_complete", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    repair_order_id: orderId,
                    service_id: serviceId,
                    report: report || "Naprawa zakończona pomyślnie.",
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Błąd podczas zatwierdzania naprawy.");
            }

            alert("Naprawa została zakończona pomyślnie.");
            fetchAppointmentList(); // Odświeżenie listy
        } catch (err) {
            alert(`Wystąpił błąd: ${err.message}`);
        }
    };

    useEffect(() => {
        fetchAppointmentList();
        fetchServices();
    }, []);

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        <div className="error-message">
            {appointmentList.length === 0 ? (
                <p>Brak umówionych serwisów</p>
            ) : (
                <div className="scroll-container-admin">
                    {appointmentList
                        .filter((appointment) => appointment.status === "Pending")
                        .map((appointment, index) => (
                            <div key={index} className="list-item"
                                style={{
                                    // display: "flex",
                                    // flexDirection: "column",
                                    // marginBottom: "20px",
                                    display: "flex",
                                    justifyContent: "space-between", // Przycisk na prawo, a zawartość na lewo
                                    alignItems: "center", // Wyśrodkowanie w pionie
                                    width: "100%", // Dopasowanie szerokości do rodzica
                                }}
                            >
                                <div>
                                    <strong>MODEL AUTA:</strong> {appointment.vehicle_model} <br />
                                    <strong>OPIS:</strong> {appointment.description} <br />
                                    <strong>STATUS:</strong> {appointment.status} <br />
                                    <strong>NUMER ZAMÓWIENIA:</strong> {appointment.order_id} <br />
                                    <strong>DATA SERWISU:</strong> {new Date(appointment.appointment_date).toLocaleString()}
                                    <div
                                        style={{
                                            display: "flex",
                                            alignItems: "center",
                                            marginTop: "10px",
                                            width: "100%"
                                        }}
                                    >
                                        <select
                                            onChange={(e) => handleServiceChange(appointment.order_id, e.target.value)}
                                            value={selectedServices[appointment.order_id] || ""}
                                            style={{
                                                padding: "10px",
                                                marginRight: "20px",
                                                fontSize: "14px",
                                            }}
                                        >
                                            <option value="" disabled>Wybierz usługę</option>
                                            {services.map((service) => (
                                                <option key={service.id} value={service.id}>
                                                    {service.name} - {service.price} PLN
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                                <div
                                    style={{
                                        display: "flex",
                                        alignItems: "center",
                                        marginTop: "10px",
                                    }}
                                >
                                    <button
                                        disabled={appointment.status === "Completed"}
                                        onClick={() => completeRepair(appointment.order_id, `Raport naprawy dla ${appointment.vehicle_model}`)}
                                        style={{
                                            padding: "10px 20px",
                                            borderRadius: "5px",
                                            marginRight: "40px",
                                            fontSize: "20px",
                                            borderRadius: "5px",
                                        }}
                                    >
                                        ZAKOŃCZ
                                    </button>
                                </div>
                            </div>
                        ))}
                </div>
            )}
        </div>
    );
};

export default AdminList;
