import requests

BASE_URL = "http://157.90.162.7:5001"


def register_user():
    """Rejestracja nowego użytkownika."""
    payload = {
        "username": "test_user1",
        "password": "password123",
        "email": "test1@example.com"
    }
    response = requests.post(f"{BASE_URL}/auth/register", json=payload)
    assert response.status_code == 201, f"Registration failed: {response.text}"
    print("User registered successfully.")
    return payload


def login_user(credentials):
    """Logowanie użytkownika i pobranie tokena."""
    response = requests.post(f"{BASE_URL}/auth/login", json=credentials)
    assert response.status_code == 200, f"Login failed: {response.text}"
    token = response.json().get("access_token")
    assert token, "Token not returned in response."
    print("User logged in successfully.")
    return token


def test_endpoints(token):
    """Testowanie kluczowych endpointów."""
    headers = {"Authorization": f"Bearer {token}"}

    # Testowanie pobierania danych użytkownika
    response = requests.get(f"{BASE_URL}/api/user_data", headers=headers)
    assert response.status_code in [200, 404], f"User data fetch failed: {response.text}"
    print("User data fetched successfully.")

    # Pobieranie zleceń napraw
    response = requests.get(f"{BASE_URL}/api/repair_orders")
    assert response.status_code == 200, f"Repair orders fetch failed: {response.text}"
    print("Repair orders fetched successfully.")

    # Tworzenie zlecenia naprawy
    repair_payload = {
        "vehicle_model": "Toyota Corolla",
        "description": "Wymiana opon",
        "appointment_date": "2025-01-12T10:00:00"
    }
    response = requests.post(f"{BASE_URL}/api/repair_orders", json=repair_payload, headers=headers)
    assert response.status_code in [201, 404], f"Repair order creation failed: {response.text}"
    repair_order_id = response.json().get("order_id") if response.status_code == 201 else None
    print("Repair order created successfully.")

    # Pobranie zajętych terminów
    response = requests.get(f"{BASE_URL}/api/appointments")
    assert response.status_code == 200, f"Appointments fetch failed: {response.text}"
    print("Appointments fetched successfully.")

    # Dodanie oceny warsztatu
    if repair_order_id:
        rating_payload = {
            "repair_order_id": repair_order_id,
            "rating": 5,
            "comment": "Świetna obsługa!"
        }
        response = requests.post(f"{BASE_URL}/api/ratings", json=rating_payload, headers=headers)
        assert response.status_code in [201, 404], f"Rating submission failed: {response.text}"
        print("Workshop rated successfully.")

    # Pobranie ocen warsztatu
    response = requests.get(f"{BASE_URL}/api/ratings")
    assert response.status_code == 200, f"Ratings fetch failed: {response.text}"
    print("Ratings fetched successfully.")

    # Oznaczenie naprawy jako zakończonej
    if repair_order_id:
        complete_payload = {
            "repair_order_id": repair_order_id,
            "report": "Naprawa zakończona pomyślnie",
            "service_id": 1
        }
        response = requests.post(f"{BASE_URL}/api/repair_complete", json=complete_payload)
        assert response.status_code in [200, 404], f"Repair completion failed: {response.text}"
        print("Repair completed successfully.")


if __name__ == "__main__":
    # Rejestracja i logowanie użytkownika
    credentials = register_user()
    token = login_user(credentials)

    # Testowanie endpointów
    test_endpoints(token)