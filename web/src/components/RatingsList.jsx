import React, { useEffect, useState } from "react";
import "./ScrollableList.css";

const ReviewList = () => {
    const [reviewList, setReviewList] = useState([]); // Stan na dane z API
    const [error, setError] = useState(null); // Obsługa błędów
    const [loading, setLoading] = useState(true); // Obsługa ładowania danych

    const fetchReviewList = async () => {
        const token = localStorage.getItem("token"); // Pobranie tokenu z localStorage

        if (!token) {
            setError("Brak tokenu. Zaloguj się ponownie.");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch("http://157.90.162.7:5001/api/ratings", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`, // Dodanie tokenu do nagłówka
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error("Nieautoryzowany. Zaloguj się ponownie.");
                } else {
                    throw new Error(`Błąd serwera: ${response.statusText}`);
                }
            }

            const data = await response.json();
            setReviewList(data); // Ustawienie danych w stanie
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false); // Wyłączenie stanu ładowania
        }
    };

    useEffect(() => {
        fetchReviewList();
    }, []); // Wywołanie przy montowaniu komponentu

    if (loading) {
        return <p>Ładowanie danych...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        // <div className="error-message">
        //     {reviewList.length === 0 ? (
        //         <p>Brak umówionych serwisów</p>
        //     ) : (
        //         <div className="scroll-review-list">
        //             {reviewList.map((rating, index) => (
        //                 <div key={index} className="list-item">
        //                     <strong>NR:</strong> {rating.id} <br />
        //                     <strong>UŻYTKOWNIK:</strong> {rating.user_id} <br />
        //                     <strong>OCENA:</strong> {rating.rating} <br />
        //                     <strong>KOMENTARZ:</strong> {rating.comment} <br />
        //                 </div>
        //             ))}
        //         </div>
        //     )}
        // </div>
        <div className="error-message">
            {reviewList.length === 0 ? (
                <p>Brak umówionych serwisów</p>
            ) : (
                <div className="scroll-review-list">
                    {reviewList.map((rating, index) => (
                        <div key={index} className="list-item">
                            <strong>NR:</strong> {rating.id} <br />
                            <strong>UŻYTKOWNIK:</strong> {rating.user_id} <br />
                            {/* <strong>OCENA:</strong> {rating.rating} <br /> */}
                            <strong>OCENA:</strong>{" "}
                            <div className="stars">
                                {Array.from({ length: 10 }).map((_, i) => (
                                    <span
                                        key={i}
                                        className={i < rating.rating ? "gold-star" : "gray-star"}
                                    >
                                        ★
                                    </span>
                                ))}
                            </div>
                            <br /><strong>KOMENTARZ:</strong> {rating.comment} <br />
                        </div>
                    ))}
                </div>
            )}
        </div>

    );
};

export default ReviewList;

