import React from "react";
import { Link } from "react-router-dom";
import "./Navbar.css";

const HomePageNavbar = () => {
    return (
        <nav className="navbar">
            <div className="logo">Car Workshop </div>
            <div className="nav-links">
                <Link to="/">STRONA GŁÓWNA</Link>
                <Link to="/login">ZALOGUJ SIĘ</Link>
                <Link to="/register">ZAREJESTRUJ SIĘ</Link>
                {/* <Link to="/review">Daj nam ocenę</Link> */}
                <Link to="/appointment">UMÓW SIĘ NA SPOTKANIE</Link>
                {/* <Link to="/profile">Profil</Link> */}
            </div>
        </nav>
    );
};

export default HomePageNavbar;
