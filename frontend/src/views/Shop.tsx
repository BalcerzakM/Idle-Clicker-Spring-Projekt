import { useState, useEffect, useCallback } from "react";
import "../css/ShopView.css";

// Interfejs zgodny z ShopItemDto z backendu
interface ShopItemDto {
	id: number;
	name: string;
	description: string;
	slotType: string;
	totalRizz: number;
	totalStrength: number;
	totalAgility: number;
	totalEndurance: number;
	totalLuck: number;
	price: number;
	imagePath: string;
}

function Shop() {
	const [items, setItems] = useState<ShopItemDto[]>([]);
	const [loading, setLoading] = useState<boolean>(true);
	const [error, setError] = useState<string | null>(null);
	const [buyingId, setBuyingId] = useState<number | null>(null); // które ID jest aktualnie kupowane

	const fetchItems = useCallback(async () => {
		try {
			setLoading(true);
			setError(null);
			const response = await fetch("http://localhost:8080/api/shop");
			if (!response.ok) throw new Error("Błąd pobierania ofert");
			const data: ShopItemDto[] = await response.json();
			setItems(data);
		} catch (err: any) {
			setError(err.message || "Nie udało się załadować sklepu");
		} finally {
			setLoading(false);
		}
	}, []);

	useEffect(() => {
		fetchItems();
	}, [fetchItems]);

	const handleBuy = async (offerId: number) => {
		try {
			setBuyingId(offerId);
			setError(null);
			const response = await fetch("http://localhost:8080/api/shop/buy", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(offerId), // wysyłamy samo ID jako Long
			});
			if (!response.ok) {
				// próba odczytania komunikatu błędu z backendu
				const errorText = await response.text();
				throw new Error(errorText || "Nie udało się kupić przedmiotu");
			}
			// po udanym zakupie odświeżamy listę ofert
			await fetchItems();
		} catch (err: any) {
			setError(err.message || "Wystąpił błąd podczas zakupu");
		} finally {
			setBuyingId(null);
		}
	};

	const handleRefresh = async () => {
		try {
			await fetch("http://localhost:8080/api/shop/refresh", { method: "POST" });
			await fetchItems();
		} catch (err: any) {
			setError("Nie udało się odświeżyć ofert");
		}
	};

	if (loading) {
		return <div className="shop-loading">Ładowanie ofert...</div>;
	}

	return (
		<div className="shop-container">
			<div className="shop-header">
				<button
					onClick={handleRefresh}
					className="shop-refresh-btn"
					title="Odśwież oferty"
				>
					Odśwież ofertę
				</button>
			</div>

			{error && <div className="shop-error">{error}</div>}

			{items.length === 0 ? (
				<p className="shop-empty">Brak dostępnych ofert. Zajrzyj później.</p>
			) : (
				<div className="shop-items-grid">
					{items.map((item) => (
						<div key={item.id} className="shop-item-card">
							<div className="shop-item-image-wrapper">
								<img
									src={item.imagePath}
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
								<p className="shop-item-price">💰 {item.price} </p>
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
	);
}

export default Shop;
