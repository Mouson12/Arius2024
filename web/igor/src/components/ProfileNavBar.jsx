import React from "react";
import { Link, useNavigate } from "react-router-dom"; // Dodajemy useNavigate
import "./Navbar.css";

const ProfileNavBar = () => {
    const navigate = useNavigate(); // Inicjalizujemy hook navigate

    const handleLogout = () => {
        // Usuwamy token z localStorage lub sessionStorage
        localStorage.removeItem("token");

        // Możesz też użyć sessionStorage, jeśli używasz tej metody
        // sessionStorage.removeItem("token");

        // Przekierowujemy użytkownika na stronę główną
        navigate("/");
        window.location.reload();
    };

    return (
        <nav className="navbar">
            <div className="logo">Car Workshop</div>
            <div className="nav-links">
                <Link to="/">STRONA GŁÓWNA</Link>
                {/* <Link to="/">WYLOGUJ SIĘ</Link> */}
                <Link to="/review">DAJ NAM OCENĘ</Link>
                <Link to="/appointment">UMÓW SIĘ NA SPOTKANIE</Link>
                <Link to="/profile">PROFIL</Link>
                <button onClick={handleLogout} className="logout-button">
                    WYLOGUJ SIĘ
                </button>
            </div>
        </nav>
    );
};

export default ProfileNavBar;
