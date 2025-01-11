import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ProfileNavBar from "./components/ProfileNavBar.jsx";
import HomePageNavbar from "./components/HomePageNavbar.jsx";
import Login from "./components/Pages/Login";
import Register from "./components/Pages/Register.jsx";
import Appointment from "./components/Pages/Appointment";
import HomePage from "./components/Pages/HomePage";
import backgroundImage from "./assets/backgroud.jpg"; // Import obrazu
import Profile from "./components/Pages/Profile";
import Review from "./components/Pages/Review";
import AdminPage from "./components/Pages/AdminPage";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Sprawdzanie tokenu w localStorage przy załadowaniu aplikacji
  useEffect(() => {
    const token = localStorage.getItem("token");
    console.log("Token z localStorage:", token); // Debugowanie
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token"); // Usuwanie tokenu
    setIsLoggedIn(false); // Resetowanie stanu logowania
  };

  return (
    <div
      className="App"
      style={{
        position: "relative",
        minHeight: "100vh",
        minWidth: "100vw",
        overflow: "hidden",
      }}
    >
      <div
        style={{
          backgroundImage: `linear-gradient(to right, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0)), url(${backgroundImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          height: "100%",
          width: "100%",
          position: "absolute",
          top: 0,
          left: 0,
          zIndex: -1,
        }}
      ></div>

      {/* Router i dynamiczny Navbar */}
      <Router>
        {isLoggedIn ? (
          <ProfileNavBar handleLogout={handleLogout} />
          // <HomePageNavbar />
        ) : (
          <HomePageNavbar />
        )}
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/appointment" element={<Appointment />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/review" element={<Review />} />
          <Route path="/admin" element={<AdminPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;

