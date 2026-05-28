import { useState, useEffect, useCallback } from "react";
import HeroPanel from "../components/HeroPanel";
import "../css/ShopView.css";
import type { ItemDto } from "../components/HeroPanel";
import type { ItemsAndStatsDto } from "../components/HeroPanel";

function Shop() {
	// stany sklepu
	const [items, setItems] = useState<ItemDto[]>([]);
	const [loading, setLoading] = useState<boolean>(true);
	const [error, setError] = useState<string | null>(null);
	const [buyingId, setBuyingId] = useState<number | null>(null);

	// stany bohatera
	const [hero, setHero] = useState<ItemsAndStatsDto | null>(null);

	// ---------- pobieranie danych ----------
	const fetchShopItems = useCallback(async () => {
		try {
			setLoading(true);
			setError(null);

			const res = await fetch("http://localhost:8080/api/shop");

			if (!res.ok) {
				throw new Error("Błąd pobierania ofert");
			}

			const data: ItemDto[] = await res.json();
			setItems(data);
		} catch (err: any) {
			setError(err.message || "Nie udało się załadować sklepu");
		} finally {
			setLoading(false);
		}
	}, []);

	const fetchCharacterData = useCallback(async () => {
		try {
			const res = await fetch("http://localhost:8080/api/character/statsItems");

			if (!res.ok) {
				throw new Error("Błąd pobierania danych postaci");
			}

			const heroData: ItemsAndStatsDto = await res.json();
			setHero(heroData);
		} catch (err: any) {
			console.error(err);
			setError(err.message);
		}
	}, []);

	useEffect(() => {
		fetchShopItems();
		fetchCharacterData();
	}, [fetchShopItems, fetchCharacterData]);

	// ---------- zakup ----------
	const handleBuy = async (offerId: number) => {
		try {
			setBuyingId(offerId);
			setError(null);

			const res = await fetch("http://localhost:8080/api/shop/buy", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(offerId),
			});

			if (!res.ok) {
				const errText = await res.text();
				throw new Error(errText || "Nie udało się kupić przedmiotu");
			}

			await fetchShopItems();
			await fetchCharacterData();
		} catch (err: any) {
			setError(err.message || "Wystąpił błąd podczas zakupu");
		} finally {
			setBuyingId(null);
		}
	};

	const handleRefresh = async () => {
		try {
			await fetch("http://localhost:8080/api/shop/refresh", {
				method: "POST",
			});

			await fetchShopItems();
		} catch (err: any) {
			setError("Nie udało się odświeżyć ofert");
		}
	};

	// ---------- sprzedaż ----------
	const handleSell = async (backpackItemId: number) => {
		try {
			const res = await fetch("http://localhost:8080/api/shop/sell", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(backpackItemId),
			});

			if (!res.ok) {
				throw new Error("Sprzedaż nieudana");
			}

			await fetchCharacterData();
		} catch (err: any) {
			setError(err.message || "Błąd sprzedaży");
		}
	};

	// ---------- equip ----------
	const handleEquip = async (backpackItemId: number, slotType: string) => {
		const equippedItem =
			hero?.equipment.find((item) => item.slotType === slotType) ?? null;

		const equipmentItemId = equippedItem?.id ?? null;

		try {
			const res = await fetch("http://localhost:8080/api/character/equip", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					equipmentItemId,
					backpackItemId,
				}),
			});

			if (!res.ok) {
				const errText = await res.text();
				throw new Error(errText);
			}

			await fetchCharacterData();
		} catch (err: any) {
			console.error(err);
			alert(err.message || "Nie udało się założyć przedmiotu");
		}
	};

	if (loading && !hero) {
		return <div className="shop-loading">Ładowanie...</div>;
	}

	return (
		<div className="shop-hero-container">
			{/* ---------- LEWA STRONA ---------- */}
			{hero && (
				<HeroPanel hero={hero} onSell={handleSell} onEquip={handleEquip} />
			)}

			{/* ---------- PRAWA STRONA ---------- */}
			<div className="shop-panel">
				<div className="shop-header">
					<h2>Sklep</h2>

					<button onClick={handleRefresh} className="shop-refresh-btn">
						Odśwież ofertę
					</button>
				</div>

				{error && <div className="shop-error">{error}</div>}

				{items.length === 0 ? (
					<p className="shop-empty">Brak dostępnych ofert.</p>
				) : (
					<div className="shop-grid">
						{items.map((item) => (
							<div key={item.id} className="shop-item-card">
								<div className="shop-item-image-wrapper">
									<img
										src={`/items/${item.imagePath}`}
										alt={item.name}
										className="shop-item-image"
										onError={(e) => (e.currentTarget.src = "/placeholder.png")}
									/>
								</div>

								<div className="shop-item-info">
									<h3 className="shop-item-name">{item.name}</h3>
									<p className="shop-item-desc">{item.description}</p>

									<div className="shop-item-stats">
										<span>💪 {item.totalStrength}</span>
										<span>🏃 {item.totalAgility}</span>
										<span>🛡️ {item.totalEndurance}</span>
										<span>🍀 {item.totalLuck}</span>
										<span>✨ {item.totalRizz}</span>
									</div>

									<p className="shop-item-price">💰 {item.price}</p>

									<button
										onClick={() => handleBuy(item.id)}
										disabled={buyingId === item.id}
										className="shop-buy-btn"
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

export default Shop;
