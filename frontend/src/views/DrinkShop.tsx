import { useState, useEffect, useCallback } from "react";
import { useCharacter } from "../context/CharacterContext";
import HeroPanel from "../components/HeroPanel";
import "../css/DrinkShopView.css";
import "../css/TooltipView.css";
import { itemTooltip, type ItemDto } from "../utils/ItemTooltip";
import {useAlert} from "../context/AlertContext.tsx";
import {useHeroActions} from "../utils/UseHeroActions.tsx";

function DrinkShop() {
    const { showError } = useAlert();
    const [items, setItems] = useState<ItemDto[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [buyingId, setBuyingId] = useState<number | null>(null);
    const { refreshCharacter } = useCharacter();

    const {
        hero,
        fetchCharacterData,
        handleUseItem,
        highlightedSlot,
        handleHoverSlot,
    } = useHeroActions();

    const fetchDrinkShopItems = useCallback(async () => {
        try {
            setLoading(true);
            const res = await fetch("http://localhost:8080/api/drinks");
            if (!res.ok) {
                const error = await res.json();
                showError(error.message || "Nie udało się wczytać ofert");
                return;
            }
            const data: ItemDto[] = await res.json();
            setItems(data);
        } catch (err: any) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setLoading(false);
        }
    }, [showError]);

    useEffect(() => {
        fetchDrinkShopItems();
        fetchCharacterData();
    }, [fetchDrinkShopItems, fetchCharacterData]);

    const handleBuy = async (offerId: number) => {
        try {
            setBuyingId(offerId);
            const res = await fetch("http://localhost:8080/api/drinks/buy", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(offerId),
            });
            if (!res.ok) {
                const error = await res.json();
                showError(error.message);
                return;
            }
            await fetchDrinkShopItems();
            await fetchCharacterData();
            await refreshCharacter();
        } catch (err: any) {
            console.error(err);
            showError("Brak połączenia z serwerem");
        } finally {
            setBuyingId(null);
        }
    };

    if (loading && !hero) {
        return <div className="shop-loading">Ładowanie...</div>;
    }

    return (
        <div className="shop-hero-container">
            {hero && (
                <HeroPanel
                    hero={hero}
                    onUseItem={handleUseItem}
                    highlightedSlot={highlightedSlot}
                    onHoverSlot={handleHoverSlot}
                />
            )}

            <div className="drink-shop-panel">
                {items.length === 0 ? (
                    <p className="drink-shop-empty">Brak dostępnych ofert.</p>
                ) : (
                    <div className="drink-shop-list">
                        {items.map((item) => (
                            <div
                                key={item.id}
                                className="drink-card"
                            >
                                <div className="drink-image">
                                    <img
                                        src={`/items/${item.imagePath}`}
                                        alt={item.itemName}
                                    />
                                    <span className="tooltip">{itemTooltip(item)}</span>
                                </div>

                                <div className="drink-info">
                                    <h3>{item.itemName}</h3>

                                    <p className="drink-description">
                                        {item.itemDescription || ""}
                                    </p>

                                    <p className="drink-effect">
                                        +{item.totalRizz > 0 && `${item.totalRizz} do Rizzu✨ `}
                                        {item.totalStrength > 0 && `${item.totalStrength} do Siły💪 `}
                                        {item.totalAgility > 0 && `${item.totalAgility} do Zwinności🏃 `}
                                        {item.totalEndurance > 0 && `${item.totalEndurance} do Wytrzymałości🛡️ `}
                                        {item.totalLuck > 0 && `${item.totalLuck} do Szczęścia🍀 `}

                                        <span className="drink-duration">
                                            na {item.durationInSeconds/60} minut
                                        </span>
                                    </p>
                                </div>

                                <div className="drink-buy">

                                    <div className="drink-price">
                                        {item.price}💰
                                    </div>

                                    <button
                                        onClick={() => handleBuy(item.id)}
                                        disabled={buyingId === item.id}
                                        className="drink-shop-buy-btn"
                                    >
                                        {buyingId === item.id ? "Kupowanie..." : "Kup"}
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default DrinkShop;