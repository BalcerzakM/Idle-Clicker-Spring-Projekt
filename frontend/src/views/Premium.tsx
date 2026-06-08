import { useCallback, useEffect, useState } from "react";
import "../css/PremiumView.css";
import { useAlert } from "../context/AlertContext";
import { useCharacter } from "../context/CharacterContext";

import smallImg from "../assets/premium/cristals_package_small.png";
import basicImg from "../assets/premium/cristals_package_basic.png";
import mediumImg from "../assets/premium/cristals_package_medium.png";
import largeImg from "../assets/premium/cristals_package_large.png";
import hugeImg from "../assets/premium/cristals_package_huge.png";
import enormousImg from "../assets/premium/cristals_package_enormous.png";

interface PremiumOfferDto {
    packageCode: string;
    price: number;
    cristals: number;
}

const packageImages: Record<string, string> = {
    SMALL: smallImg,
    BASIC: basicImg,
    MEDIUM: mediumImg,
    LARGE: largeImg,
    HUGE: hugeImg,
    ENORMOUS: enormousImg,
};

function Premium() {
    const { showError } = useAlert();
    const { refreshCharacter } = useCharacter();

    const [offers, setOffers] = useState<PremiumOfferDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [buyingCode, setBuyingCode] = useState<string | null>(null);

    const fetchOffers = useCallback(async () => {
        try {
            setLoading(true);

            const res = await fetch("http://localhost:8080/api/premium");

            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Nie udało się wczytać ofert premium");
                return;
            }

            const data: PremiumOfferDto[] = await res.json();
            setOffers(data);
        } catch (err) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setLoading(false);
        }
    }, [showError]);

    useEffect(() => {
        fetchOffers();
    }, [fetchOffers]);

    const handleBuy = async (packageCode: string) => {
        try {
            setBuyingCode(packageCode);

            const res = await fetch("http://localhost:8080/api/premium/buyCristals", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(packageCode),
            });

            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Nie udało się kupić kryształów");
                return;
            }

            await refreshCharacter();
        } catch (err) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setBuyingCode(null);
        }
    };

    const formatPrice = (priceInGrosze: number) =>
        `${(priceInGrosze / 100).toFixed(2)} zł`;

    if (loading) {
        return <div className="premium-loading">Ładowanie...</div>;
    }

    return (
        <div className="premium-container">
            <div className="premium-panel">
                <div className="premium-header">
                    <h1>Sklep Premium</h1>
                </div>

                <div className="premium-grid">
                    {offers.map((offer) => (
                        <div key={offer.packageCode} className="premium-card">
                            <img
                                src={packageImages[offer.packageCode]}
                                alt="Pakiet kryształów"
                                className="premium-package-img"
                            />

                            <div className="premium-crystals">
                                {offer.cristals} kryształów
                            </div>

                            <div className="premium-price">
                                {formatPrice(offer.price)}
                            </div>

                            <button
                                className="premium-buy-btn"
                                onClick={() => handleBuy(offer.packageCode)}
                                disabled={buyingCode === offer.packageCode}
                            >
                                {buyingCode === offer.packageCode ? "Kupowanie..." : "Kup"}
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Premium;