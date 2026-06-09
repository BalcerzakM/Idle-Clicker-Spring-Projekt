import { useState, useEffect, useCallback } from 'react';
import { useAlert } from "../context/AlertContext";
import { useCharacter } from "../context/CharacterContext"; // Zakładam, że chcesz odświeżyć postać po zakupie
import '../css/CarDealer.css';

export interface BaseVehicleDto {
    id: number;
    name: string;
    imagePath: string;
    price: number;
    timeReductionPercent: number;
}

const VehicleShop = () => {
    const { showError, showInfo} = useAlert();
    const { refreshCharacter } = useCharacter();

    const [vehicles, setVehicles] = useState<BaseVehicleDto[]>([]);
    const [currentIndex, setCurrentIndex] = useState<number>(0);
    const [rentDays, setRentDays] = useState<number>(1);
    
    const [loading, setLoading] = useState<boolean>(true);
    const [isRenting, setIsRenting] = useState<boolean>(false);

    const fetchVehicles = useCallback(async () => {
        try {
            setLoading(true);
            const res = await fetch("http://localhost:8080/api/vehicles");
            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Nie udało się wczytać pojazdów na parkingu");
                return;
            }
            const data: BaseVehicleDto[] = await res.json();
            setVehicles(data);
        } catch (err: any) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setLoading(false);
        }
    }, [showError]);

    useEffect(() => {
        fetchVehicles();
    }, [fetchVehicles]);

    const handleNext = () => {
        setCurrentIndex((prev) => (prev + 1) % vehicles.length);
        setRentDays(1); // Reset suwaka przy zmianie auta
    };

    const handlePrev = () => {
        setCurrentIndex((prev) => (prev - 1 + vehicles.length) % vehicles.length);
        setRentDays(1);
    };

    const handleRent = async () => {
        if (vehicles.length === 0) return;
        const currentVehicle = vehicles[currentIndex];

        try {
            setIsRenting(true);
            const res = await fetch(`http://localhost:8080/api/vehicles/${currentVehicle.id}/rent?days=${rentDays}`, {
                method: "POST",
            });

            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Wystąpił błąd podczas wynajmu.");
                return;
            }

            showInfo("Pojazd pomyślnie wynajęty!");

            if (refreshCharacter) {
                await refreshCharacter();
            }

        } catch (err: any) {
            console.error(err);
            showError("Brak połączenia z serwerem podczas transakcji");
        } finally {
            setIsRenting(false);
        }
    };

    if (loading) {
        return <div className="loading-screen">Ładowanie parkingu...</div>;
    }

    if (vehicles.length === 0) {
        return <div className="loading-screen">Brak pojazdów na parkingu!</div>;
    }

    const currentVehicle = vehicles[currentIndex];
    const totalPrice = currentVehicle.price * rentDays;

    return (
        <div className="carDealer">
            <div className="carousel-viewport">
                <div 
                    className="carousel-track" 
                    style={{ transform: `translateX(-${currentIndex * 100}%)` }}
                >
                    {vehicles.map((vehicle) => (
                        <div className="car-slide" key={vehicle.id}>
                            <img 
                                src={`/vehicles/${vehicle.imagePath}`}
                                alt={vehicle.name} 
                                className="car-image" 
                                onError={(e) => (e.currentTarget.src = "/placeholder.png")}
                            />
                        </div>
                    ))}
                </div>
            </div>

            <button className="nav-button prev-button" onClick={handlePrev}>&#9664;</button>
            <button className="nav-button next-button" onClick={handleNext}>&#9654;</button>

            <div className="info-panel">
                <h2>{currentVehicle.name}</h2>
                <p>Skraca czas misji o: <strong>{currentVehicle.timeReductionPercent}%</strong></p>
                
                <div className="slider-container">
                    <label>Czas wynajmu: {rentDays} {rentDays === 1 ? 'dzień' : 'dni'}</label>
                    <input 
                        type="range" 
                        min="1" 
                        max="7" 
                        value={rentDays} 
                        onChange={(e) => setRentDays(Number(e.target.value))}
                    />
                </div>

                <div className="price-display">
                    Koszt: <strong>{totalPrice} Kryształów</strong>
                </div>

                <button 
                    className="rent-button" 
                    onClick={handleRent}
                    disabled={isRenting}
                >
                    {isRenting ? "Przetwarzanie..." : "Wynajmij"}
                </button>
            </div>
        </div>
    );
};

export default VehicleShop;